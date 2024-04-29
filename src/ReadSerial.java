package WeatherStation.src;

import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;
import java.util.Arrays;
import java.util.TimerTask;

public class ReadSerial extends TimerTask {

    private static String tempHum = "";

    private static double[] tempHumArray = {0.0, 0.0};

    private  SerialPort sp;

    ReadSerial(SerialPort sp){
        this.sp = sp;
    }

    @Override
    public void run() {
        //James Andrew Smith and Richard Robinson, 3 January 2024, Blog: Java and Arduino: Serial Communication
        //https://www.yorku.ca/professor/drsmith/2024/01/03/java-and-arduino-serial-communication/
        //Reads serial port information when available from arduino IDE program.
        //If bytes are available, read them until done
        while(sp.bytesAvailable() > 0){
            try {
                tempHum += (char)sp.getInputStream().read();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        //Converts temperature and humidity information received to a double array.
        if (tempHum != "" && tempHum.length() ==13){
            tempHumArray = Arrays.stream(tempHum.split(",")).mapToDouble(Double::parseDouble).toArray();
        }
        tempHum = "";
        //End of referenced code
    }

    public static double[] getTempHumArray(){
        return tempHumArray;
    }
}
