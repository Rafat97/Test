/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arduinoSensors;

import static arduinoSensors.CSVCretor.RELATION;
import static arduinoSensors.CSVCretor.TOTAL_DATA;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.markers.SeriesMarkers;

/**
 *
 * @author rafat
 */
public class test {

    public static void main(String[] args) throws IOException {

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        XYChart chart = new XYChartBuilder().width((int) screenSize.getWidth()).height((int) (screenSize.getHeight() * 0.8)).title("Real Time Plot").xAxisTitle("Time").yAxisTitle("Sensors").build();

        // Customize Chart
        chart.getStyler().setLegendVisible(true);
        
    List<Double> xData = new ArrayList<>();
            xData.add(2.0);
             xData.add(1.0);
              xData.add(0.0);

        chart.addSeries("MQ_8", xData, xData);
 
    // Show it
    
        JFrame frame = new SwingWrapper(chart).displayChart();
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
//    new SwingWrapper(chart).displayChart();
      
            
         
    }

    /**
     * Generates a set of random walk data
     *
     * @param numPoints
     * @return
     */
    private static double[] getRandomWalk(int numPoints) {

        double[] y = new double[numPoints];
        y[0] = 0;
        for (int i = 1; i < y.length; i++) {
            y[i] = y[i - 1] + Math.random() - .5;
        }
        return y;
    }
}
