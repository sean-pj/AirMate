package WeatherStation.src;

import com.fazecast.jSerialComm.SerialPort;
import java.io.IOException;
import java.util.TimerTask;

public class StateMachine extends TimerTask {

    private static SerialPort sp;
    private static long timeSave = 0;
    private static boolean fanState = false;
    StateMachine(SerialPort sp){
        this.sp = sp;
    }
    @Override
    public void run() {
        //Statemachine of the overall program. Controls when the fan should turn on in response to sensor data.

        //Ensures servo is off
        try {
            sp.getOutputStream().write(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //If air is humid, hot, and air quality is good turn on the fan.
        if ((MainClass.airQualPin.getValue() < 40) && (ReadSerial.getTempHumArray()[0] > 50 || ReadSerial.getTempHumArray()[1] > 25) && (timeSave == 0 || System.currentTimeMillis() > (timeSave + 10000)) && !fanState){
            timeSave = System.currentTimeMillis();
            OledUpdate.fanString = "Fan On";
            fanState = true;
            //Writes to serial port to turn on servo through arduino IDE program
            for (int i = 0; i < 2; i++){
                try {
                    sp.getOutputStream().write(-1);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            //If air is cool, dry or the air quality is bad with the fan already on, turn the fan back off
        } else if (((MainClass.airQualPin.getValue() > 40) || (ReadSerial.getTempHumArray()[0] < 50 && ReadSerial.getTempHumArray()[1] < 25)) && (System.currentTimeMillis() > (timeSave + 2500)) && fanState){
            OledUpdate.fanString = "Fan Off";
            fanState = false;
            for (int i = 0; i < 2; i++){
                try {
                    sp.getOutputStream().write(1);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }
}
