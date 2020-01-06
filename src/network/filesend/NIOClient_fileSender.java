/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network.filesend;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import static java.lang.Character.UnicodeBlock.of;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import org.apache.commons.io.FilenameUtils;
import static org.hamcrest.CoreMatchers.is;
import org.json.simple.JSONObject;

/**
 *
 * @author rafat
 */
public class NIOClient_fileSender {

    public static String ipAddress = "192.168.127.1";
    public static int portNumber = 1112;
    

    public static void main(String[] args) throws IOException, InterruptedException {
        
        JFileChooser jFileChooser = new JFileChooser(FileSystemView.getFileSystemView());
        jFileChooser.setMultiSelectionEnabled(false);
        jFileChooser.setAcceptAllFileFilterUsed(false);
        jFileChooser.setDialogTitle("Select a .txt file");
        // only allow files of .txt extension 
        FileNameExtensionFilter restrict = new FileNameExtensionFilter(
                "Only supported(*jpg,*jpeg,*.txt;*.csv)",
                "jpg","jpeg","txt", "csv"
        );
        jFileChooser.addChoosableFileFilter(restrict);
        jFileChooser.setFileFilter(restrict);
        jFileChooser.showSaveDialog(null);
        File selectedFile = new File(jFileChooser.getSelectedFile().getAbsolutePath());

        Path path = Paths.get(selectedFile.getAbsolutePath());
        BasicFileAttributes fileAttribute = Files.readAttributes(path, BasicFileAttributes.class);

        JSONObject fileInfo = new JSONObject();
        fileInfo.put("name", selectedFile.getName());
        fileInfo.put("size", fileAttribute.size());
        fileInfo.put("extension", FilenameUtils.getExtension(selectedFile.getAbsolutePath()));
        fileInfo.put("mime", Files.probeContentType(path));
//        fileInfo.put("creationTime", fileAttribute.creationTime());
//        fileInfo.put("lastAccessTime", fileAttribute.lastAccessTime());
//        fileInfo.put("lastModifiedTime", fileAttribute.lastModifiedTime());

        InetSocketAddress crunchifyAddr = new InetSocketAddress(ipAddress, portNumber);
        SocketChannel crunchifyClient = SocketChannel.open(crunchifyAddr);

        ArrayList<String> filedata = new ArrayList<String>();
        filedata.add(fileInfo.toJSONString());

//               companyDetails.add("finish");
        String data = "#1#" + filedata.get(0);
        System.out.println(data.length());
        byte[] message = new String(data).getBytes();
        ByteBuffer bufferString = ByteBuffer.wrap(message);
        crunchifyClient.write(bufferString);
        log("sending: " + data);
        bufferString.clear();
        Thread.sleep(10);

        FileChannel fileChannel = FileChannel.open(path);
        ByteBuffer buffer = ByteBuffer.allocate(NIOserver_FileReciver.BUFFER_SIZE);
        while (fileChannel.read(buffer) > 0) {
            buffer.flip();
            crunchifyClient.write(buffer);
            buffer.clear();
        }
//        readFileChannel(selectedFile.getAbsolutePath(),crunchifyClient);

//                ByteBuffer crunchifyBuffer = ByteBuffer.allocate(128);
        //		crunchifyClient.read(crunchifyBuffer);
        //		String result = new String(crunchifyBuffer.array()).trim();
        //                System.out.println("Respose = "+result);
        crunchifyClient.close();
    }

    public static void readFileChannel(String file, SocketChannel crunchifyClient) throws IOException, InterruptedException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(file,
                "rw");
        FileChannel fileChannel = randomAccessFile.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        Charset charset = Charset.forName("US-ASCII");
        while (fileChannel.read(byteBuffer) > 0) {
            byteBuffer.rewind();
            Thread.sleep(1);
            crunchifyClient.write(byteBuffer);
//            System.out.print(charset.decode(byteBuffer));
            byteBuffer.flip();
        }
        fileChannel.close();
        randomAccessFile.close();
    }

    private static void log(String str) {
        System.out.println(str);
    }
}
