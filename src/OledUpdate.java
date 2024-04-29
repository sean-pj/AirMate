package WeatherStation.src;

import org.firmata4j.ssd1306.SSD1306;

import java.util.TimerTask;

public class OledUpdate extends TimerTask {

    private SSD1306 oledObj;
    public static String fanString = "Fan: off";

    public static String apiText1 = "";

    public static String apiText2 = "";

    private static int goodAirQual = 40;

    private static int modAirQual = 50;
    OledUpdate(SSD1306 oledObj){
        this.oledObj = oledObj;
    }

    @Override
    public void run() {
        //James Andrew Smith, 2024, EECS 1021 Lab G
        //Updates OLED Board
        long airQual = MainClass.airQualPin.getValue();
        oledObj.clear();
        String outputString = airQualityToString(airQual);
        //Air quality
        oledObj.getCanvas().drawString(0,0, "Air Quality: " + outputString);
        oledObj.getCanvas().drawString(0, 10, fanString);
        //Only suggest to open windows it is not raining, it is warm outside, and the air quality is moderate or worse
        if (airQual > goodAirQual && WeatherAPI.getPrecip() < 0.2f && WeatherAPI.getTemp() > 10f){
            oledObj.getCanvas().drawString(0, 10, fanString + "//Open Window");
        } else{
            oledObj.getCanvas().drawString(0, 10, fanString);
        }
        //Prints humidity and temperature, along with selected API info
        oledObj.getCanvas().drawString(0,20, "Indoor Temp: " + ReadSerial.getTempHumArray()[1]);
        oledObj.getCanvas().drawString(0,30, "Humidity: " + ReadSerial.getTempHumArray()[0] + "%");
        oledObj.getCanvas().drawString(0, 40, apiText1);
        oledObj.getCanvas().drawString(0,50, apiText2);
        oledObj.display();
        //End of referenced code
    }

    public static String airQualityToString(long airQual){
        //State machine for air quality output to OLED board.
        //Ensures that air quality data is user readable
        String outputString;
        if (airQual <= goodAirQual){
            outputString = "Good";
        } else if (airQual < modAirQual){
            outputString = "Moderate";
        }else {
            outputString = "Bad";
        }
        return outputString;
    }
}
