/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xboxcontroller;

import ch.aplu.xboxcontroller.XboxController;
import ch.aplu.xboxcontroller.XboxControllerAdapter;
import javax.swing.JOptionPane;
import org.python.util.*;

/**
 *
 * @author Root10
 */
public class XBoxController {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
     
 
        XboxController xc;
        xc = new XboxController();

        if (!xc.isConnected()) {
            System.out.println("Not Connected");
        } else {
            System.out.println("Connected");
        }

        xc.addXboxControllerListener(new XboxControllerAdapter() {
            @Override
            public void isConnected(boolean connected) {
                System.out.println("isConnected "+connected);
            }

            @Override
            public void rightThumbDirection(double direction) {
                System.out.println("rightThumbDirection "+direction);
            }

            @Override
            public void rightThumbMagnitude(double magnitude) {
                 System.out.println("rightThumbMagnitude "+magnitude);
            }

            @Override
            public void leftThumbDirection(double direction) {
               System.out.println("leftThumbDirection "+direction);
            }

            @Override
            public void leftThumbMagnitude(double magnitude) {
               System.out.println("leftThumbMagnitude "+magnitude);
            }

            @Override
            public void dpad(int dpad, boolean pressed) {
               System.out.println("dpad "+dpad+" "+pressed);
            }

            @Override
            public void rightThumb(boolean pressed) {
                System.out.println("rightThumb "+pressed);
            }

            @Override
            public void leftThumb(boolean pressed) {
                System.out.println("leftThumb "+pressed);
            }

            @Override
            public void rightShoulder(boolean pressed) {
                System.out.println("rightShoulder "+pressed);
            }

            @Override
            public void leftShoulder(boolean pressed) {
                System.out.println("leftShoulder "+pressed);
            }

            @Override
            public void start(boolean pressed) {
                 System.out.println("start "+pressed);
            }

            @Override
            public void back(boolean pressed) {
                 System.out.println("back "+pressed);
            }

            @Override
            public void buttonY(boolean pressed) {
                 System.out.println("buttonY "+pressed);
            }

            @Override
            public void buttonX(boolean pressed) {
                 System.out.println("buttonX "+pressed);
            }

            @Override
            public void buttonB(boolean pressed) {
                 System.out.println("buttonB "+pressed);
            }

            @Override
            public void buttonA(boolean pressed) {
                 System.out.println("buttonA "+pressed);
            }
            
            @Override
            public void leftTrigger(double pressed) {
                System.out.println("left "+pressed);
            }
            
            @Override
            public void rightTrigger(double pressed) {
                 System.out.println("right "+pressed);
            }
        });

    }

}
