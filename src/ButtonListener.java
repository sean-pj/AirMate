package WeatherStation.src;

import org.firmata4j.IODeviceEventListener;
import org.firmata4j.IOEvent;

import java.io.IOException;

public class ButtonListener implements IODeviceEventListener {

    @Override
    public void onStart(IOEvent ioEvent) {

    }

    @Override
    public void onStop(IOEvent ioEvent) {

    }

    @Override
    public void onPinChange(IOEvent ioEvent) {
        //James Andrew Smith, 2024 , EECS 1021, Lab H
        if (ioEvent.getPin().getIndex() == MainClass.buttonPin.getIndex()) {
            //Ends program on button press
            try {
                MainClass.end();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        if(ioEvent.getPin().getIndex() == MainClass.potPin.getIndex()){
            //Controls API weather information displayed on OLED based on potentiometer rotation
            if(MainClass.potPin.getValue()==0){
                WeatherAPI.updateWeather();
                System.out.println("Updating API Info");
            } else if (MainClass.potPin.getValue() <= 341) {
                OledUpdate.apiText1 = "Outdoor Temp: " + WeatherAPI.getTemp() + " C";
                OledUpdate.apiText2 = "Precipitation: " + WeatherAPI.getPrecip() + " mm";
            } else if (MainClass.potPin.getValue() <= 682){
                OledUpdate.apiText1 = "Max Temp: " + WeatherAPI.getMaxTemp() + " C";
                OledUpdate.apiText2 = "Min Temp:" + WeatherAPI.getMinTemp() + " C";
            } else {
                OledUpdate.apiText1 = "Precip Prob: " + WeatherAPI.getPrecipPercent() + " %";
                OledUpdate.apiText2 = "Wind Speed: " + WeatherAPI.getWindSpeed() + " m/s";
            }
        }
        //End of referenced code
    }
    @Override
    public void onMessageReceive(IOEvent ioEvent, String s) {

    }
}
