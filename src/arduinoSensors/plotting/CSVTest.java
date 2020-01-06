/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arduinoSensors.plotting;

import arduinoSensors.Sensor;
import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileWriter;

/**
 *
 * @author rafat
 */
public class CSVTest {

    public static void main(String[] args) {
        File file = new File("running.csv");
        try {
            // create FileWriter object with file as parameter 
            FileWriter outputfile = new FileWriter(file);

            // create CSVWriter object filewriter object as parameter 
            CSVWriter writer = new CSVWriter(outputfile);

            // adding header to csv 
            String[] header = {"Time","A", "B", "C"};
            writer.writeNext(header);
            writer.close();

            int i =0;
            
            while (true) {
                outputfile = new FileWriter(file, true);
                writer = new CSVWriter(outputfile);

                String[] data = {i+"",Math.random() * 1000 + "", Math.random() * 10000 + "", Math.random() * 100 + ""};
                writer.writeNext(data);
                writer.close();
                System.out.println(i++);
                Thread.sleep(1000);
                

            }

            // closing writer connection 
        } catch (Exception e) {
            // TODO Auto-generated catch block 
            e.printStackTrace();
        }
    }
}
