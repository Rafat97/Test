/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package camera;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.ds.gstreamer.GStreamerDriver;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import xboxcontroller.WebcamViewerExample;


/**
 *
 * @author rafat
 */
public class MainClass extends JFrame{
 public static void main(String[] args) {
     
     
     new Thread(new Runnable() {
         @Override
         public void run() {
              SwingUtilities.invokeLater(new MainWebcamViewer(0));
         }
     }).start();
     new Thread(new Runnable() {
         @Override
         public void run() {
               SwingUtilities.invokeLater(new MainWebcamViewer(1));
         }
     }).start();
    
       
        
    }
}
