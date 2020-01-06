/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arduinoSensors;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFrame;

/**
 *
 * @author rafat
 */
public class MainClass {

    public static void main(String[] args) throws IOException {
        
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new SensorData();
                frame.pack();
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setVisible(true);
            }
        });
        th.start();

    }
}
