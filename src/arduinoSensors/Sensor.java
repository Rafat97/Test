/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arduinoSensors;

import com.fazecast.jSerialComm.SerialPort;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

/**
 *
 * @author rafat
 */
public class Sensor {

    public static String[] ALL_SENSORS_LIST = {
        "Runing time",
        "MQ-8",
        "MQ-9",
        "Moisture",
        "UV",
        "Humidity",
        "Temperature",
        "Pressure",
        "Approx Altitude",
        "pH"
            
    };

    public static ArrayList<String> RAW_DATA;

    public static ArrayList<Double> TIME;
    public static ArrayList<Double> MQ_8;
    public static ArrayList<Double> MQ_9;
    public static ArrayList<Double> MOISTURE;
    public static ArrayList<Double> UV;
    public static ArrayList<Double> HUMIDITY;
    public static ArrayList<Double> TEMPERATURE;
    public static ArrayList<Double> PRESSURE;
    public static ArrayList<Double> APPROX_ALTITUDE;

    public static String CSV_FILENAME;
    public static String CSV_FILE_DIR_NAME;
    public static String CSV_DIR_NAME;
    
    
    public static String[] getPortNames() {
        SerialPort[] ports = SerialPort.getCommPorts();
        String[] result = new String[ports.length];
        for (int i = 0; i < ports.length; i++) {
            result[i] = ports[i].getSystemPortName();
        }
        return result;

    }

}
