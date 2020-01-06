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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

/**
 *
 * @author rafat
 */
public class SensorRealTimePlot {

    int lastData = 0;

    public SensorRealTimePlot(int lastData) {
        this.lastData = lastData;
    }

    public void RealTimePlot_Maker() throws IOException {
        String dir = Sensor.CSV_FILE_DIR_NAME;

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        XYChart chart = new XYChartBuilder().width((int) screenSize.getWidth()).height((int) (screenSize.getHeight() * 0.8)).title("Real Time Plot").xAxisTitle("Time").yAxisTitle("Sensors").build();

        chart.getStyler().setLegendVisible(true);

        Reader reader = Files.newBufferedReader(Paths.get(dir));
        CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(5).build();
        List<String[]> records = csvReader.readAll();

        ArrayList<Double> Time = new ArrayList<Double>();
        ArrayList<Double> mq_8 = new ArrayList<Double>();
        ArrayList<Double> mq_9 = new ArrayList<Double>();
        ArrayList<Double> MOISTURE = new ArrayList<Double>();
        ArrayList<Double> UV = new ArrayList<Double>();
        ArrayList<Double> HUMIDITY = new ArrayList<Double>();
        ArrayList<Double> TEMPERATURE = new ArrayList<Double>();
        ArrayList<Double> PRESSURE = new ArrayList<Double>();
        ArrayList<Double> APPROX_ALTITUDE = new ArrayList<Double>();

        
        
        for (String[] record : records) {

            Time.add(Double.parseDouble(record[0]));
            mq_8.add(Double.parseDouble(record[1]));
            mq_9.add(Double.parseDouble(record[2]));
            MOISTURE.add(Double.parseDouble(record[3]));
            UV.add(Double.parseDouble(record[4]));
            HUMIDITY.add(Double.parseDouble(record[5]));
            TEMPERATURE.add(Double.parseDouble(record[6]));
            PRESSURE.add(Double.parseDouble(record[7]));
            APPROX_ALTITUDE.add(Double.parseDouble(record[8]));
        }

        List<Double> x = Time.subList(Time.size() - lastData, Time.size() - 1);
        List<Double> y = mq_8.subList(mq_8.size() - lastData, mq_8.size() - 1);
        chart.addSeries("MQ_8", x, y);
        y.clear();

        y = mq_9.subList(mq_9.size() - lastData, mq_9.size() - 1);
        chart.addSeries("MQ_9", x, y);
        y.clear();

        y = MOISTURE.subList(MOISTURE.size() - lastData, MOISTURE.size() - 1);
        chart.addSeries("MOISTURE", x, y);
        y.clear();

        y = UV.subList(UV.size() - lastData, UV.size() - 1);
        chart.addSeries("UV", x, y);
        y.clear();

//        chart.addSeries("MQ_8", Time, mq_8);
//        chart.addSeries("MQ_9", Time, mq_9);
//        chart.addSeries("MOISTURE", Time, mq_9);
        final SwingWrapper<XYChart> sw = new SwingWrapper<XYChart>(chart);
        JFrame frame = sw.displayChart();
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);

        while (true) {

            try {
                Thread.sleep(1000);
            } catch (Exception ex) {
                Logger.getLogger(SensorRealTimePlot.class.getName()).log(Level.SEVERE, null, ex);
            }

            javax.swing.SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {

                    Reader reader;
                    try {
                        reader = Files.newBufferedReader(Paths.get(dir));
                        CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(5).build();
                        List<String[]> records = csvReader.readAll();
                        ArrayList<Double> Time = new ArrayList<Double>();
                        ArrayList<Double> mq_8 = new ArrayList<Double>();
                        ArrayList<Double> mq_9 = new ArrayList<Double>();
                        ArrayList<Double> MOISTURE = new ArrayList<Double>();
                        ArrayList<Double> UV = new ArrayList<Double>();
                        ArrayList<Double> HUMIDITY = new ArrayList<Double>();
                        ArrayList<Double> TEMPERATURE = new ArrayList<Double>();
                        ArrayList<Double> PRESSURE = new ArrayList<Double>();
                        ArrayList<Double> APPROX_ALTITUDE = new ArrayList<Double>();

                        for (String[] record : records) {
                            Time.add(Double.parseDouble(record[0]));
                            mq_8.add(Double.parseDouble(record[1]));
                            mq_9.add(Double.parseDouble(record[2]));
                            MOISTURE.add(Double.parseDouble(record[3]));
                            UV.add(Double.parseDouble(record[4]));
                            HUMIDITY.add(Double.parseDouble(record[5]));
                            TEMPERATURE.add(Double.parseDouble(record[6]));
                            PRESSURE.add(Double.parseDouble(record[7]));
                            APPROX_ALTITUDE.add(Double.parseDouble(record[8]));
                        }

                        List<Double> x = Time.subList(Time.size() - lastData, Time.size() - 1);
                        List<Double> y = mq_8.subList(mq_8.size() - lastData, mq_8.size() - 1);
                        chart.updateXYSeries("MQ_8", x, y, null);

                        y = mq_9.subList(mq_9.size() - lastData, mq_9.size() - 1);
                        chart.updateXYSeries("MQ_9", x, y, null);
                        y.clear();

                        y = MOISTURE.subList(MOISTURE.size() - lastData, MOISTURE.size() - 1);
                        chart.updateXYSeries("MOISTURE", x, y, null);
                        y.clear();

                        y = UV.subList(UV.size() - lastData, UV.size() - 1);
                        chart.updateXYSeries("UV", x, y, null);
                        y.clear();

//                        chart.updateXYSeries("MQ_8", Time, mq_8, null);
//                        chart.updateXYSeries("MQ_9", Time, mq_9, null);
//                        chart.updateXYSeries("MOISTURE", Time, MOISTURE, null);
                        sw.repaintChart();

                    } catch (IOException ex) {
                        Logger.getLogger(SensorRealTimePlot.class.getName()).log(Level.SEVERE, null, ex);
                    }

//                    
//                   List<Double> Time = Sensor.TIME.subList(Sensor.TIME.size()-11 , Sensor.TIME.size());
//                    List<Double> MQ_8 = Sensor.MQ_8.subList(Math.max(Sensor.MQ_8.size() - 15, 0), Sensor.MQ_8.size());
//                    chart.updateXYSeries("MQ_9", Sensor.TIME, Sensor.MQ_9, null);
//                    chart.updateXYSeries("MOISTURE", Sensor.TIME, Sensor.MOISTURE, null);
//                    chart.updateXYSeries("UV", Sensor.TIME, Sensor.UV, null);
//                    chart.updateXYSeries("HUMIDITY", Sensor.TIME, Sensor.HUMIDITY, null);
//                    chart.updateXYSeries("TEMPERATURE", Sensor.TIME, Sensor.TEMPERATURE, null);
//                    chart.updateXYSeries("PRESSURE", Sensor.TIME, Sensor.PRESSURE, null);
//                    chart.updateXYSeries("APPROX_ALTITUDE", Sensor.TIME, Sensor.APPROX_ALTITUDE, null);
                }
            });
        }
    }

    public static void main(String[] args) throws IOException {
        new SensorRealTimePlot(10).RealTimePlot_Maker();
    }

}
