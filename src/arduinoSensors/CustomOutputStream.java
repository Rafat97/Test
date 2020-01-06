/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arduinoSensors;

import java.io.IOException;
import java.io.OutputStream;
import javax.swing.JTextArea;

/**
 *
 * @author rafat
 */
public class CustomOutputStream extends OutputStream {

    private JTextArea textArea;
    private boolean isScroll = true;

    public CustomOutputStream(JTextArea textArea) {
        this.textArea = textArea;
    }
    public CustomOutputStream(JTextArea textArea,boolean isScroll) {
        this.textArea = textArea;
        this.isScroll = isScroll;
    }

    public void setIsScroll(boolean isScroll) {
        this.isScroll = isScroll;
    }

    
    
    @Override
    public void write(int b) throws IOException {
        // redirects data to the text area
        textArea.append(String.valueOf((char) b));
        // scrolls the text area to the end of data
        if(isScroll){
             textArea.setCaretPosition(textArea.getDocument().getLength());
        }
       
        // keeps the textArea up to date
//        textArea.update(textArea.getGraphics());
    }

}
