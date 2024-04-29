package WeatherStation;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class WeatherAPI {
    private static final String cityAPI = "https://api.open-meteo.com/v1/forecast?latitude=43.7001&longitude=-79.4163&current=temperature_2m,precipitation,wind_speed_10m&daily=temperature_2m_max,temperature_2m_min,precipitation_probability_mean&timezone=America%2FNew_York";

    private static double temp;
    private static double precip;

    private static double maxTemp;

    private static double minTemp;
    private static double precipPercent;

    private static double windSpeed;

    public static double getTemp(){
        return  temp;
    }

    public static double getPrecip(){
        return precip;
    }

    public static double getMaxTemp(){
        return maxTemp;
    }
    public static double getMinTemp(){
        return minTemp;
    }

    public static double getPrecipPercent(){
        return precipPercent;
    }

    public static double getWindSpeed(){
        return windSpeed;
    }


    public static void updateWeather(){
        try {
            //Updates weather information from Open-Mateo public API
            // ADAPTED code from Daniel, Random Code on Youtube, September 26 2021
            //Reference https://gist.github.com/Da9el00/e8b1c2e5185e51413d9acea81056c2f9
            //https://www.youtube.com/watch?v=zZoboXqsCNw

            //URL for API
            URL url = new URL(cityAPI);

            //HTTP URL connection
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //Get information rather than send information
            conn.setRequestMethod("GET");
            //Try to make a connection
            conn.connect();

            //Check if connect is made
            int responseCode = conn.getResponseCode();

            // 200 OK
            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {

                //Since if statement is past, a connection has successfully been made
                StringBuilder informationString = new StringBuilder();
                //Gets information from URL
                Scanner scanner = new Scanner(url.openStream());

                //Keep gathering information in string builder until done
                while (scanner.hasNext()) {
                    informationString.append(scanner.nextLine());
                }
                //Close the scanner
                scanner.close();

                //JSON google code library used to convert strings to JSON
                JSONParser parse = new JSONParser();
                JSONObject weatherObject = (JSONObject) parse.parse(String.valueOf(informationString));

                //End of referenced code

                //Values from API converted to doubles.
                //First uses keys to get the appropriate info, then methods to convert JSON object to a string or interpret arrays, then parse to double.
                temp = Double.parseDouble(String.valueOf(((JSONObject)weatherObject.get("current")).get("temperature_2m")));
                precip = Double.parseDouble(String.valueOf(((JSONObject)weatherObject.get("current")).get("precipitation")));
                maxTemp = Double.parseDouble(((JSONObject)weatherObject.get("daily")).get("temperature_2m_max").toString().split(",")[0].substring(1));
                minTemp = Double.parseDouble(((JSONObject)weatherObject.get("daily")).get("temperature_2m_min").toString().split(",")[0].substring(1));
                precipPercent = Double.parseDouble(((JSONObject)weatherObject.get("daily")).get("precipitation_probability_mean").toString().split(",")[0].substring(1));
                windSpeed = Double.parseDouble(String.valueOf(((JSONObject)weatherObject.get("current")).get("wind_speed_10m")));
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

}
