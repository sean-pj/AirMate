package WeatherStation.src;

import edu.princeton.cs.introcs.StdDraw;

import java.util.ArrayList;
import java.util.TimerTask;

public class GraphUpdate extends TimerTask {

    private static ArrayList<Integer> xAirData = new ArrayList<Integer>();
    private static ArrayList<Integer> yAirData = new ArrayList<Integer>();

    private static ArrayList<Integer> xTempData = new ArrayList<>();
    private static ArrayList<Integer> yTempData = new ArrayList<>();

    private static ArrayList<Integer> xHumData = new ArrayList<>();
    private static ArrayList<Integer> yHumData = new ArrayList<>();

    private int airCounter;
    private int tempCounter;

    private Graph airQualGraph;
    private Graph tempGraph;

    GraphUpdate(Graph airQualGraph, Graph tempGraph){
        this.airQualGraph = airQualGraph;
        this.tempGraph = tempGraph;
        airCounter = airQualGraph.getxAxisScale()[1];
        tempCounter = tempGraph.getxAxisScale()[1];

    }

    @Override
    public void run() {
        //Updates live graphs periodically using sensor data.
        //This is very similar to my EECS 1021 Main Project, to save time.

        airQualGraph.clear();
        //Keeps adding sensor data to ArrayList of points
        //Once data gets too large for the graph to display, remove the oldest value to make space.
        if(xAirData.size() == airQualGraph.getxAxisScale()[1] + 1){
            xHumData.replaceAll(integer-> integer - 1);
            xTempData.replaceAll(integer -> integer - 1);
            xAirData.replaceAll(integer -> integer - 1);
            xHumData.removeLast();
            yHumData.removeLast();
            xTempData.removeLast();
            yTempData.removeLast();
            xAirData.removeLast();
            yAirData.removeLast();
            xTempData.addFirst(tempGraph.getxAxisScale()[1]);
            xAirData.addFirst(airQualGraph.getxAxisScale()[1]);
            xHumData.addFirst(tempGraph.getxAxisScale()[1]);
        } else{
            xTempData.add(tempCounter);
            xAirData.add(airCounter);
            xHumData.add(tempCounter);
            airCounter--;
            tempCounter--;
        }

        //Updates graph with sensor data
        try {
            //Converts Air Quality value to a 0-100 value
            double slope = 100f / (80f - 37f);
            double convertedQuality = slope * ((double)MainClass.airQualPin.getValue() - 37);
            //Updates graph data
            yAirData.addFirst((int) convertedQuality);
            yTempData.addFirst((int) ReadSerial.getTempHumArray()[1]);
            yHumData.addFirst((int)ReadSerial.getTempHumArray()[0]);
            //Displays graph data
            airQualGraph.drawGraph(xAirData, yAirData);
            tempGraph.setLineColor(StdDraw.RED);
            tempGraph.drawGraph(xTempData, yTempData);
            tempGraph.setLineColor(StdDraw.GREEN);
            tempGraph.drawGraph(xHumData,yHumData);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}