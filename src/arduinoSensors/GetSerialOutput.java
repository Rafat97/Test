/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arduinoSensors;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortPacketListener;
import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rafat
 */
public class GetSerialOutput {

    void Output() throws IOException {

        SerialPort comPort = SerialPort.getCommPorts()[0];
        if (!comPort.isOpen()) {
            System.out.println(Sensor.TIME.size());
        }
        comPort.openPort();

        DateFormat simple = new SimpleDateFormat("dd-MMM-yyyy_HH-mm-ss_SSS_Z");
        Date result = new Date(System.currentTimeMillis());

        String Filenname = "sensor_" + simple.format(result) + ".csv";
        String dir = System.getProperty("user.dir");
        String dirSensor = dir + "/" + Filenname;

        Sensor.CSV_FILENAME = Filenname;
        Sensor.CSV_DIR_NAME = dir;
        Sensor.CSV_FILE_DIR_NAME = dirSensor;

        File file = new File(Sensor.CSV_FILE_DIR_NAME);
        if (!file.exists()) {
            file.createNewFile();
        }

        FileWriter outputfile = new FileWriter(new File(Sensor.CSV_FILE_DIR_NAME), true);
        CSVWriter writer = new CSVWriter(outputfile);

        writer.writeNext(Sensor.ALL_SENSORS_LIST);
        writer.close();

        comPort.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 0, 0);
        try {

            String content = "";
            while (true) {

                while (comPort.bytesAvailable() == 0) {
                    Thread.sleep(20);
                }

                InputStream in = comPort.getInputStream();
                String singleData = (char) in.read() + "";
                if (singleData.equals("\n")) {
                    System.out.println(content);

                    String[] data = content.split(",");
                    if (data.length == Sensor.ALL_SENSORS_LIST.length) {

                        Sensor.RAW_DATA.add(content);

//                        Sensor.TIME.add(Double.parseDouble(data[0]));
//                        Sensor.MQ_8.add(Double.parseDouble(data[1]));
//                        Sensor.MQ_9.add(Double.parseDouble(data[2]));
//                        Sensor.MOISTURE.add(Double.parseDouble(data[3]));
//                        Sensor.UV.add(Double.parseDouble(data[4]));
//                        Sensor.HUMIDITY.add(Double.parseDouble(data[5]));
//                        Sensor.TEMPERATURE.add(Double.parseDouble(data[6]));
//                        Sensor.PRESSURE.add(Double.parseDouble(data[7]));
//                        Sensor.APPROX_ALTITUDE.add(Double.parseDouble(data[8]));

                        file = new File(Sensor.CSV_FILE_DIR_NAME);
                        outputfile = new FileWriter(file, true);
                        writer = new CSVWriter(outputfile);
                        writer.writeNext(data);
                        writer.close();
                    }

                    content = "";
                } else {
                    content += singleData;
                }

                in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        comPort.closePort();
    }

}
