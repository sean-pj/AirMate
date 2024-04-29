package WeatherStation.src;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class Graph {

    private String yLabel;
    private String xLabel;
    private String title;
    private String yLabelUnit;
    public static int[] xCanvasScale = {-3, 100};
    public static int[] yCanvasScale = {-1, 5};
    private int[] xAxisScale = {0, 100};
    private int[] yAxisScale = {0, 5};
    private Color axisColor = StdDraw.BLUE;
    private Color lineColor = StdDraw.BLUE;

    Graph(String xLabel, String yLabel, String title){
        this.xLabel = xLabel;
        this.yLabel = yLabel;
        this.title = title;
    }

    public void setAxisColor(Color color){
        axisColor = color;
    }

    public void setLineColor(Color color){
        lineColor = color;
    }

    public void setxCanvasScale(int[] scale){
        xCanvasScale = scale;
    }

    public void setyCanvasScale(int[] scale){
        yCanvasScale = scale;
    }

    public void setxAxisScale(int[] scale){
        xAxisScale = scale;
    }

    public void setyAxisScale(int[] scale){
        yAxisScale = scale;
    }

    public int[] getxCanvasScale(){
        return xCanvasScale;
    }

    public int[] getyCanvasScale(){
        return yCanvasScale;
    }

    public int[] getxAxisScale(){
        return xAxisScale;
    }

    public int[] getyAxisScale(){
        return yAxisScale;
    }

    public void drawGraph(ArrayList<Integer> xData, ArrayList<Integer> yData) throws InterruptedException {
        /*
    Draws and displays a graph using StdDraw from the princeton standard library.
    Similar to my EECS 1021 Main Project to save on time. Adjusted to allow for two separate graphs.
     */

        StdDraw.setXscale(xCanvasScale[0], xCanvasScale[1]); // x for the canvas (box)
        StdDraw.setYscale(yCanvasScale[0], yCanvasScale[1]);// y for the canvas (box)

        //Axes (y and x)
        StdDraw.setPenColor(axisColor);
        StdDraw.line(xAxisScale[0],yAxisScale[0],xAxisScale[0],yAxisScale[1]);
        StdDraw.line(xAxisScale[0],0,xAxisScale[1],0);

        //Titles and labels
        StdDraw.text((xAxisScale[1] + xAxisScale[0]) / 2, yAxisScale[1] + 2, title);
        StdDraw.text((double) (xAxisScale[1] + xAxisScale[0]) / 2, yAxisScale[0] -(yAxisScale[1] * 0.03), xLabel);
        StdDraw.text(xAxisScale[0] - (xAxisScale[1] * 0.1), (double) yAxisScale[1] / 2,yLabel,90);
        StdDraw.text(xAxisScale[1] * 0.95f, yAxisScale[0] - (yAxisScale[1] * 0.04), LocalTime.now().truncatedTo(ChronoUnit.SECONDS).toString());
        StdDraw.text(xAxisScale[0] * 1.05f,yAxisScale[0] - (yAxisScale[1] * 0.04), LocalTime.now().minusSeconds(xAxisScale[1] - xAxisScale[0]).truncatedTo(ChronoUnit.SECONDS).toString());
        StdDraw.text(xAxisScale[0] - (xAxisScale[1] * 0.04), yAxisScale[1], Integer.toString(yAxisScale[1]));

        //Line Appearance
        StdDraw.setPenColor(lineColor);
        StdDraw.setPenRadius(0.005);

        //Draw Line and Points
        for(int i = 1; i < xData.size(); i++){
            StdDraw.text(xData.get(i - 1),yData.get(i - 1), "o");
            StdDraw.line(xData.get(i - 1), yData.get(i - 1), xData.get(i), yData.get(i));
        }
    }

    public void clear(){
        //Clear graph
        StdDraw.clear();
    }
}
