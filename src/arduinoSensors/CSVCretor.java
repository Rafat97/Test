/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arduinoSensors;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

/**
 *
 * @author rafat
 */
public class CSVCretor {

    public static void writeDataLineByLine(String filePath) {
        // first create file object for file placed at location 
        // specified by filepath 
        File file = new File(filePath);
        try {
            // create FileWriter object with file as parameter 
            FileWriter outputfile = new FileWriter(file);

            // create CSVWriter object filewriter object as parameter 
            CSVWriter writer = new CSVWriter(outputfile);

            // adding header to csv 
            String[] header = {"Name", "Class", "Marks"};
            writer.writeNext(header);

            // add data to csv 
            String[] data1 = {"Aman", "10", "620"};
            writer.writeNext(data1);
            String[] data2 = {"Suraj", "10", "630"};
            writer.writeNext(data2);

            // closing writer connection 
            writer.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block 
            e.printStackTrace();
        }
    }
    public static int TOTAL_DATA = 0;
    public static int RELATION = 8;
    
    
    
    public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException {
        String dir = Sensor.CSV_FILE_DIR_NAME;
//
//        File file = new File(dir);
//
//        BufferedReader br = new BufferedReader(new FileReader(file));
//
//        String st;
//        while ((st = br.readLine()) != null) {
//            System.out.println(st);
//        }

        ArrayList<Double> time = new ArrayList<>();
        ArrayList<Double> s1 = new ArrayList<>();
        Reader reader = Files.newBufferedReader(Paths.get(dir));
//        CSVReader csvReader = new CSVReader(reader);
        CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
        List<String[]> records = csvReader.readAll();
        TOTAL_DATA = records.size();
        for (String[] record : records) {
            System.out.println(Double.parseDouble(record[0]));
            time.add(Double.parseDouble(record[0] + ""));
            s1.add(Double.parseDouble(record[RELATION] + ""));
        }
        final XYChart chart = QuickChart.getChart("Simple XChart Real-time Demo", "Time", "MQ-8", "MQ-8", time, s1);
        final SwingWrapper<XYChart> sw = new SwingWrapper<XYChart>(chart);
        
        
        sw.displayChart();

        records.clear();
        System.out.println(TOTAL_DATA);
        while (true) {

            Reader reader2 = Files.newBufferedReader(Paths.get(dir));
            csvReader = new CSVReaderBuilder(reader2).withSkipLines(1).build();
            records = csvReader.readAll();

            int totaldifference = records.size() - TOTAL_DATA;
            if (totaldifference >= 1) {
                for (String[] record : records.subList(TOTAL_DATA, records.size())) {

                    time.add(Double.parseDouble(record[0]));
                    s1.add(Double.parseDouble(record[RELATION]));

                }
                TOTAL_DATA = records.size();
                chart.updateXYSeries("MQ-8", time, s1, null);
                sw.repaintChart();
            }

            System.out.println("");

        }

    }

}
