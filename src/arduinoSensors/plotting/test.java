/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arduinoSensors.plotting;

import arduinoSensors.SensorData;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import java.awt.BorderLayout;
import java.awt.Color;
import static java.awt.Frame.MAXIMIZED_BOTH;
import java.awt.GraphicsConfiguration;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.stage.FileChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import sun.java2d.SunGraphicsEnvironment;

/**
 *
 * @author rafat
 */
public class test {

    public static int x = 0;
    public static int y = 1;

    public static void main(String[] args) throws IOException {

// create the line graph
        JFrame window = new JFrame();
        window.setTitle("Sensor Graph GUI");

        JFileChooser jFileChooser = new JFileChooser(FileSystemView.getFileSystemView());
        jFileChooser.setMultiSelectionEnabled(false);
        jFileChooser.setAcceptAllFileFilterUsed(false);
        jFileChooser.setDialogTitle("Select a sensor csv file");
        // only allow files of .txt extension 
        FileNameExtensionFilter restrict = new FileNameExtensionFilter(
                "Only supported(*.csv)",
                "csv"
        );
        jFileChooser.addChoosableFileFilter(restrict);
        jFileChooser.setFileFilter(restrict);
        jFileChooser.showSaveDialog(null);
        File selectedFile = new File(jFileChooser.getSelectedFile().getAbsolutePath());

        GraphicsConfiguration config = window.getGraphicsConfiguration();
        Rectangle usableBounds = SunGraphicsEnvironment.getUsableBounds(config.getDevice());
        window.setMaximizedBounds(usableBounds);
        window.setExtendedState(MAXIMIZED_BOTH);

        window.setSize(600, 400);

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Reader reader = Files.newBufferedReader(Paths.get(selectedFile.getAbsolutePath()));
        CSVReader csvReader = new CSVReader(reader);
        List<String[]> records = csvReader.readAll();
        String[] Headers = records.get(0);

        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries[] series1 = new XYSeries[Headers.length];
        for (int i = 0; i < Headers.length; i++) {
            series1[i] = new XYSeries(Headers[i]);
            dataset.addSeries(series1[i]);
        }

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Sensor Chart", // Chart title
                "Time", // X-Axis Label
                "All sensor values", // Y-Axis Label
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        XYPlot plot = chart.getXYPlot();
//
//        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        plot.setBackgroundPaint(Color.BLACK);
//        plot.setRenderer(renderer);
        plot.setRangeGridlinesVisible(false);
        plot.setDomainGridlinesVisible(false);

        chart.getLegend().setFrame(BlockBorder.NONE);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBackground(Color.red);

        window.add(chartPanel);

        window.setVisible(true);

        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                int itt = 0;
                while (true) {

                    Reader reader;
                    try {

                        reader = Files.newBufferedReader(Paths.get(selectedFile.getAbsolutePath()));
                        CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
                        List<String[]> records = csvReader.readAll();

                        for (String[] record : records) {
                            Thread.sleep(100);
                            for (int i = 0; i < Headers.length; i++) {
                                series1[i].add(Double.parseDouble(record[0]), Double.parseDouble(record[i]));
                            }
                        }

                    } catch (Exception ex) {
                        System.out.println(ex);

                    }

//                    Random rand = new Random();
////                    series.add(x,series,y);
//
////                    for (String coloumn : Headers) {
////                        dataset.addValue(y, coloumn, x + "");
////                    }
//
//                    double xy = Math.random() * 1000;
//                    x++;
//                    y = (int) xy;
//                    if (x > 20) {
//                        dataset.removeColumn(0);
////                        series.remove(0);
//                    }
//
//                    try {
//                        Thread.sleep(100);
//                    } catch (Exception ex) {
//                        Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
//                    }
                }

            }
        });
        th.start();
    }
}
