/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arduinoSensors.views;

import static arduinoSensors.CSVCretor.RELATION;
import arduinoSensors.SensorRealTimePlot;
import arduinoSensors.Sensor;
import arduinoSensors.SensorData;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;

import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.Styler.LegendPosition;
import org.knowm.xchart.style.colors.XChartSeriesColors;
import org.knowm.xchart.style.lines.SeriesLines;
import org.knowm.xchart.style.markers.SeriesMarkers;

/**
 *
 * @author rafat
 */
public class TableFrame extends javax.swing.JFrame {

    /**
     * Creates new form TableFream
     */
    ArrayList<Double> X;
    ArrayList<Double> Y;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

//    Sensor.CSV_FILE_DIR_NAME  "sensor_1577873152992.csv" ;
    String rootPath = Sensor.CSV_FILE_DIR_NAME;
    int SkipLineCSV = 5;

    public TableFrame() throws IOException {
        initComponents();
        setLocationRelativeTo(null);
        DataCSV();

    }

    void DataCSV() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();

        if (model.getRowCount() >= 1) {
            model.setColumnCount(0);
            model.setRowCount(0);
        }
        try {
            Reader reader = Files.newBufferedReader(Paths.get(rootPath));
            CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(SkipLineCSV).build();
            List<String[]> records = csvReader.readAll();

            for (String object : Sensor.ALL_SENSORS_LIST) {
                model.addColumn(object);
            }
            Object[] objects = new Object[Sensor.ALL_SENSORS_LIST.length + 1];

            for (String[] record : records) {
                //System.out.println(Double.parseDouble(record[0]));

                
                for (int i = 0; i < Sensor.ALL_SENSORS_LIST.length ; i++) {
                    objects[i] = Double.parseDouble(record[i]);
                }
                
//                objects[0] = Double.parseDouble(record[0]);
//                objects[1] = Double.parseDouble(record[1]);
//                objects[2] = Double.parseDouble(record[2]);
//                objects[3] = Double.parseDouble(record[3]);
//                objects[4] = Double.parseDouble(record[4]);
//                objects[5] = Double.parseDouble(record[5]);
//                objects[6] = Double.parseDouble(record[6]);
//                objects[7] = Double.parseDouble(record[7]);
//                objects[8] = Double.parseDouble(record[8]);
//                objects[9] = Double.parseDouble(record[9]);

                model.addRow(objects);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e , "Alert", JOptionPane.WARNING_MESSAGE);
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }

    }

    void DataCSV_to_Plot(int xAxis, int yAxis, String Title, String xTitle, String yTitle) {
        try {

            X = new ArrayList<>();
            Y = new ArrayList<>();
            Reader reader = Files.newBufferedReader(Paths.get(rootPath));
            CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(SkipLineCSV).build();
            List<String[]> records = csvReader.readAll();

            for (String[] record : records) {
                X.add(Double.parseDouble(record[xAxis] + ""));
                Y.add(Double.parseDouble(record[yAxis] + ""));
            }
            final XYChart chart = QuickChart.getChart(Title, xTitle, yTitle, Title, X, Y);
            chart.getStyler().setLegendVisible(true);

            final SwingWrapper<XYChart> sw = new SwingWrapper<XYChart>(chart);

            Thread th = new Thread(new Runnable() {
                @Override
                public void run() {
                    SwingWrapper sw = new SwingWrapper(chart);

                    JFrame frame = sw.displayChart();
                    frame.pack();
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.setVisible(true);
                }
            });
            th.start();

        } catch (Exception e) {
            
            JOptionPane.showMessageDialog(this, e.getMessage() , "Alert", JOptionPane.WARNING_MESSAGE);
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));

        }

    }

    void DataCSV_to_MultiPlot() {

        try {
            List<XYChart> charts = new ArrayList<XYChart>();

            X = new ArrayList<>();
            Y = new ArrayList<>();
            Reader reader = Files.newBufferedReader(Paths.get(rootPath));
            CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(SkipLineCSV).build();
            List<String[]> records = csvReader.readAll();

            for (String[] record : records) {
                X.add(Double.parseDouble(record[0] + ""));
            }

            for (int i = 0; i < Sensor.ALL_SENSORS_LIST.length; i++) {
                XYChart chart = new XYChartBuilder().xAxisTitle("Time").yAxisTitle(Sensor.ALL_SENSORS_LIST[i]).width((int) (screenSize.getWidth() / 3.0)).height((int) (screenSize.getHeight() / 3.0)).build();
                chart.getStyler().setLegendVisible(true);
//                chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideN);
//                chart.getStyler().setYAxisMin(-10.0);
//                chart.getStyler().setYAxisMax(10.0);
                Y.clear();
                for (String[] record : records) {
                    Y.add(Double.parseDouble(record[i] + ""));
                }

                XYSeries series = chart.addSeries(Sensor.ALL_SENSORS_LIST[i] + " / Time", X, Y);

                series.setLabel("Test");

                series.setLineColor(XChartSeriesColors.BLUE);
                series.setMarkerColor(Color.LIGHT_GRAY);
                series.setMarker(SeriesMarkers.NONE);
                series.setLineStyle(SeriesLines.SOLID);

//                series.setMarker(SeriesMarkers.NONE);
                charts.add(chart);
            }
            Thread th = new Thread(new Runnable() {
                @Override
                public void run() {

                    SwingWrapper sw = new SwingWrapper<XYChart>(charts);
                    JFrame frame = sw.displayChartMatrix("All Sensor Data Plots");

                    frame.pack();
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.setVisible(true);

                }
            });
            th.start();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage() , "Alert", JOptionPane.WARNING_MESSAGE);
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }

    }
    SwingWrapper RealTime;

    private void DataCSV_to_RealtimeChart(int lastData) {
        // Create Chart 
        try {

            Thread th = new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        new SensorRealTimePlot(lastData).RealTimePlot_Maker();
                    } catch (IOException ex) {
                        Logger.getLogger(TableFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            });
            th.start();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage() , "Alert", JOptionPane.WARNING_MESSAGE);
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }

    }

    private void openDialog(Frame f) {
        final JDialog dialog = new JDialog(f, "Please Select The Sensor", true);
        final JButton button = new JButton("SUBMIT");

        JComboBox mbComboBox = new JComboBox(Sensor.ALL_SENSORS_LIST);
//        mbComboBox.setBounds(50, 50, 90, 20);
        JPanel panel = new JPanel();
        panel.add(mbComboBox);
        panel.add(button);
        JButton[] buttons = {button};
        JOptionPane optionPane = new JOptionPane(panel,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null, buttons, button);

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                System.out.println(button.getActionCommand());
                //System.out.println(mbComboBox.getSelectedIndex()); 
                int index = mbComboBox.getSelectedIndex();
                dialog.dispose();
                DataCSV_to_Plot(0, index, Sensor.ALL_SENSORS_LIST[index] + "/Time", "Time", Sensor.ALL_SENSORS_LIST[index]);
            }
        });

        dialog.getContentPane().add(optionPane);
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(f);
        dialog.setVisible(true);

    }

    private void openDialog2(Frame f) {
        final JDialog dialog = new JDialog(f, "last Updated Data", true);
        final JButton button = new JButton("SUBMIT");
        String[] data = {"10", "20", "30", "40", "50", "60", "70", "80", "90"};
        JComboBox mbComboBox = new JComboBox(data);
//        mbComboBox.setBounds(50, 50, 90, 20);
        JPanel panel = new JPanel();
        panel.add(mbComboBox);
        panel.add(button);
        JButton[] buttons = {button};
        JOptionPane optionPane = new JOptionPane(panel,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null, buttons, button);

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                System.out.println(button.getActionCommand());
                //System.out.println(mbComboBox.getSelectedIndex()); 
                int index = (mbComboBox.getSelectedIndex() + 1) * 10;
                dialog.dispose();
                if (Sensor.RAW_DATA.size() > (index + 10)) {
                    DataCSV_to_RealtimeChart(index);
                } else {
                    openDialog3(f);
                }

            }
        });

        dialog.getContentPane().add(optionPane);
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(f);
        dialog.setVisible(true);

    }

    private void openDialog3(Frame f) {
        JDialog d = new JDialog(f, "Error Dialog", true);
        d.setLayout(new FlowLayout());
        JButton b = new JButton("OK");
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                d.dispose();
                d.setVisible(false);
            }
        });
        d.add(new JLabel("Sorry! Not enough data"));
        d.add(b);
        d.setSize(300, 150);
        d.setLocationRelativeTo(f);
        d.setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTable1.setEnabled(false);
        jScrollPane1.setViewportView(jTable1);

        jButton1.setText("Single Plot Create");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Refresh Table");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Multiple Plot Create");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Real Time Chart");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 733, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        openDialog(this);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        DataCSV();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        DataCSV_to_MultiPlot();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

        openDialog2(this);
    }//GEN-LAST:event_jButton4ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TableFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TableFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TableFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TableFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new TableFrame().setVisible(true);
                } catch (Exception ex) {
                    
                  
                    
                    System.exit(0);
                    Logger
                            .getLogger(TableFrame.class
                                    .getName()).log(Level.SEVERE, null, ex);
                }
            }

        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
