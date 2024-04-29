package WeatherStation.src;

import com.fazecast.jSerialComm.SerialPort;
import org.firmata4j.Pin;
import org.firmata4j.firmata.FirmataDevice;
import org.junit.Test;

import java.io.IOException;
import java.util.Timer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestClass {
    //Learned in class from James Andrew Smith, EECS 1021
    @Test
    public void MQ135() throws IOException, InterruptedException {
        //Tests air quality sensor values, and ensures that air quality sensor logic for the oled board is functioning
        var groveBoard = new FirmataDevice("COM3");
        groveBoard.start();
        groveBoard.ensureInitializationIsDone();
        Pin airQualPin = groveBoard.getPin(15);
        for (int i = 0; i < 5; i++){
            //Test values
            Thread.sleep(1000);
            long value = airQualPin.getValue();
            System.out.println(value);
            assertTrue("",value >= 0 && value < 200);
        }
        assertEquals("Error: Incorrect air quality message ", OledUpdate.airQualityToString(40), "Good");
        assertEquals("Error: Incorrect air quality message ", OledUpdate.airQualityToString(45), "Moderate");
        assertEquals("Error: Incorrect air quality message ", OledUpdate.airQualityToString(100), "Bad");
    }

    @Test
    public void DHT22() throws InterruptedException {
        //For this test ensure you are in a room temperature room.
        //Tests temperature and humidity sensor is giving appropriate values
        //Also ensures the jSerialComm logic initializes properly to communicate with a separate arduino device
        SerialPort sp = SerialPort.getCommPort("COM5");
        sp.setComPortParameters(9600, Byte.SIZE, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
        sp.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0,0);

        var hasOpened = sp.openPort();
        if (!hasOpened){
            throw new IllegalStateException("We failed to open the serial usb port check other programs (Ard IDE, etc)");
        }

        Timer timer = new Timer();
        ReadSerial testTask = new ReadSerial(sp);
        timer.schedule(testTask, 0, 10);
        Thread.sleep(5000);
        //Is the temperature humidity sensor returning appropriate values?
        System.out.println(ReadSerial.getTempHumArray()[0]);
        System.out.println(ReadSerial.getTempHumArray()[1]);
        assertTrue("Error: incorrect humidity value", ReadSerial.getTempHumArray()[0] >= 0 && ReadSerial.getTempHumArray()[0]<= 100);
        assertTrue("Error: incorrect temperature value", ReadSerial.getTempHumArray()[1] > 10 && ReadSerial.getTempHumArray()[1] < 30);

    }

    @Test
    public void weatherAPI(){
        //Tests ensures weather API is giving appropriate values
        WeatherAPI.updateWeather();
        System.out.println(WeatherAPI.getTemp() + " Celsius");
        System.out.println(WeatherAPI.getPrecip() + " mm precipitation");
        assertTrue("Error: weather api returning incorrect values for temperature, or its really cold",WeatherAPI.getTemp() >= 0 && WeatherAPI.getTemp() < 50);
        assertTrue("Error: Weather api returning incorrect precipitation values",WeatherAPI.getPrecip() >= 0 && WeatherAPI.getPrecip() < 5);
    }
    //End of referenced code
}
