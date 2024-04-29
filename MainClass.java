package WeatherStation;

import com.fazecast.jSerialComm.SerialPort;
import edu.princeton.cs.introcs.StdDraw;
import org.firmata4j.I2CDevice;
import org.firmata4j.IODevice;
import org.firmata4j.Pin;
import org.firmata4j.firmata.FirmataDevice;
import org.firmata4j.ssd1306.SSD1306;
import java.io.IOException;
import java.util.Timer;

public class MainClass {
    public static Pin airQualPin;

    public static Pin buttonPin;

    public static Pin potPin;
    private static SSD1306 oledObj;

    private static SerialPort sp;

    private static Timer oledTimer = new Timer();
    private static Timer serialTimer = new Timer();
    private static Timer graphTimer = new Timer();

    private static Timer stateTimer = new Timer();

    private static IODevice groveBoard;

    public static void main(String[] args) throws InterruptedException, IOException {

        //Starting grove board and assigning pins
        groveBoard = new FirmataDevice("COM3");
        groveBoard.start();
        groveBoard.ensureInitializationIsDone();
        airQualPin = groveBoard.getPin(15);
        buttonPin = groveBoard.getPin(6);
        potPin = groveBoard.getPin(14);
        buttonPin.setMode(Pin.Mode.INPUT);

        //Event listener for button
        //James Andrew Smith, 2024 , EECS 1021, Lab H
        groveBoard.addEventListener(new ButtonListener());
        //End of referenced code

        //Oled board initialization
        //James Andrew Smith, 2024, EECS 1021 Lab G
        I2CDevice i2c = groveBoard.getI2CDevice((byte)0x3c);
        oledObj = new SSD1306(i2c, SSD1306.Size.SSD1306_128_64);
        oledObj.init();
        //End of referenced code

        //Serial port initialization
        //James Andrew Smith and Richard Robinson, 3 January 2024, Blog: Java and Arduino: Serial Communication
        //https://www.yorku.ca/professor/drsmith/2024/01/03/java-and-arduino-serial-communication/
        //Start of referenced code
        sp = SerialPort.getCommPort("COM5");
        sp.setComPortParameters(9600, Byte.SIZE, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
        sp.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0,0);
        var hasOpened = sp.openPort();
        if (!hasOpened){
            throw new IllegalStateException("We failed to open the serial usb port check other programs (Ard IDE, etc)");
        }
        //End of referenced code

        //Initial weather API updates. From now on, to update API info, a manual input is required on the potentiometer
        WeatherAPI.updateWeather();

        //Setting up graph objects for sensor data

        //Air quality graph setup
        Graph airQualGraph = new Graph("[time (s)]", "Air Quality [0-100]", "Air Quality over Time");
        airQualGraph.setLineColor(StdDraw.GRAY);
        airQualGraph.setyCanvasScale(new int[] {-1, 100});
        airQualGraph.setxCanvasScale(new int[] {-3, 50});
        airQualGraph.setxAxisScale(new int[] {0,20});
        airQualGraph.setyAxisScale(new int[] {0, 100});

        //Temperature and humidity graph setup
        Graph tempHumGraph = new Graph("[time (s)]", "Temp(Red)Hum(Green)", "Temp(C)Hum(%) over Time");
        tempHumGraph.setxAxisScale(new int[] {30,50});
        tempHumGraph.setyAxisScale(new int[] {0, 100});

        //Create tasks for serial port reading, oled board updating, graph updating, and the state machine.
        var serialTask = new ReadSerial(sp);
        var oledTask = new OledUpdate(oledObj);
        var graphTask = new GraphUpdate(airQualGraph, tempHumGraph);
        var stateTask = new StateMachine(sp);

        //Start a timer for each task
        oledTimer.schedule(oledTask,0,2000);
        serialTimer.schedule(serialTask,0, 1000);
        graphTimer.schedule(graphTask, 0,1000);
        stateTimer.schedule(stateTask,0, 2000);
    }

    public static void end() throws IOException, InterruptedException {
        //Ends tasks and closes grove board
        oledTimer.cancel();
        serialTimer.cancel();
        graphTimer.cancel();
        Thread.sleep(2000);
        groveBoard.stop();
        sp.closePort();
        System.exit(0);
    }
}
