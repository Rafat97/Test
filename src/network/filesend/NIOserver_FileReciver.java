/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network.filesend;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import network.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FilenameUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author rafat
 */
public class NIOserver_FileReciver {

    
    private Selector selector;
    private Map<SocketChannel, List> dataMapper;
    private InetSocketAddress listenAddress;
    public static final int BUFFER_SIZE = 100 * 1024;

    public File StoredFile;

    public static void main(String[] args) throws Exception {
        Runnable server = new Runnable() {
            @Override
            public void run() {
                try {

                    ServerSocket s = new ServerSocket(0);
                    int port = s.getLocalPort();
                    InetAddress inetAddress = InetAddress.getLocalHost();
                    System.out.println("IP Address:- " + inetAddress.getHostAddress());
                    System.out.println("Host Name:- " + inetAddress.getHostName());
                    System.out.println("default port number:- " + port);
                    System.out.println("our port number:- " + 1112);
                    new NIOserver_FileReciver(inetAddress.getHostAddress(), 1112).startServer();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };

        new Thread(server).start();

    }

    public NIOserver_FileReciver(String address, int port) throws IOException {
        listenAddress = new InetSocketAddress(address, port);
        dataMapper = new HashMap<SocketChannel, List>();
    }

    // create server channel    
    private void startServer() throws IOException {
        this.selector = Selector.open();
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);

        // retrieve server socket and bind to port
        serverChannel.socket().bind(listenAddress);
        serverChannel.register(this.selector, SelectionKey.OP_ACCEPT);

        while (true) {
            System.out.println("i'm a server and i'm waiting for new connection and buffer select...");
            // wait for events
            this.selector.select();

            //work on selected keys
            Iterator keys = this.selector.selectedKeys().iterator();
            while (keys.hasNext()) {
                SelectionKey key = (SelectionKey) keys.next();

                // this is necessary to prevent the same key from coming up 
                // again the next time around.
                keys.remove();

                if (!key.isValid()) {
                    continue;
                }

                if (key.isAcceptable()) {
                    this.accept(key);
                } else if (key.isReadable()) {
                    this.read(key);
                }
            }
        }
    }

    //accept a connection made to this channel's socket
    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel channel = serverChannel.accept();
        channel.configureBlocking(false);
        Socket socket = channel.socket();
        SocketAddress remoteAddr = socket.getRemoteSocketAddress();
        System.out.println("Connected to: " + remoteAddr);

        // register channel with selector for further IO
        dataMapper.put(channel, new ArrayList());
        channel.register(this.selector, SelectionKey.OP_READ);
    }

    //read from the socket channel
    private void read(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        int numRead = -1;
        numRead = channel.read(buffer);

        if (numRead == -1) {
            this.dataMapper.remove(channel);
            Socket socket = channel.socket();
            SocketAddress remoteAddr = socket.getRemoteSocketAddress();
            System.out.println("Connection closed by client: " + remoteAddr);
            channel.close();
            key.cancel();
            return;
        }

        byte[] data = new byte[numRead];
        System.arraycopy(buffer.array(), 0, data, 0, numRead);
        System.out.println("Got: " + new String(data));
        filterData(buffer, new String(data), data, channel);
    }

    void filterData(ByteBuffer buffer, String data, byte[] bytedata, SocketChannel channel) {
        if (data.length() >= 5) {

            String getdata = new String(data);
            String firstFourChars = "";
            firstFourChars = getdata.substring(0, 3);

            if (firstFourChars.equals("#1#")) {
                System.out.println("TRUE");
                try {
                    System.out.println(getdata.substring(3, getdata.length()));
                    JSONParser parser = new JSONParser();
                    JSONObject jsonObject = (JSONObject) parser.parse(getdata.substring(3, getdata.length()));

                    String name = (String) jsonObject.get("name");

                    DateFormat simple = new SimpleDateFormat("dd-MMM-yyyy_HH-mm-ss_SSS_Z");
                    Date result = new Date(System.currentTimeMillis());

                    String Filenname = FilenameUtils.removeExtension(name) + "_" + simple.format(result) + "." + FilenameUtils.getExtension(name);
                    Filenname = "receved_files/" + Filenname;
                    File f = new File(Filenname);

                    if (!f.exists()) {
                        f.createNewFile();
                    }
                    StoredFile = f;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            } else {

                try {

                    //Path path = Paths.get(StoredFile.getAbsolutePath());
//                    Path path = Paths.get("C:\\recive\\1.csv");
//                    FileChannel fileChannel = FileChannel.open(path,
//                            EnumSet.of(StandardOpenOption.CREATE,
//                                    StandardOpenOption.TRUNCATE_EXISTING,
//                                    StandardOpenOption.WRITE)
//                    );
                    writeFileChannel(buffer, data, bytedata, channel);

                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }
        }

    }

    public void writeFileChannel(ByteBuffer buffer, String data, byte[] bytedata, SocketChannel channel) throws IOException {

        File file = StoredFile;

        OutputStream fr
                = new FileOutputStream(file,true);

        // Starts writing the bytes in it 
        fr.write(bytedata);

//        FileWriter fr = new FileWriter(file, true);
//        BufferedWriter br = new BufferedWriter(fr);
//        PrintWriter pr = new PrintWriter(br);
//        pr.print(data);
//        pr.flush();
//        br.flush();
//        fr.flush();
//        pr.close();
//        br.close();
        fr.flush();
        fr.close();

//        Path path = Paths.get("C:\\recive\\temp.txt");
//        FileChannel fileChannel = FileChannel.open(path, EnumSet.of(StandardOpenOption.CREATE,
//                StandardOpenOption.TRUNCATE_EXISTING,
//                StandardOpenOption.WRITE));
//        Charset charset = Charset.forName("US-ASCII");
//        
//        while (socketChannel.read(byteBuffer) > 0) {
//            byteBuffer.flip();
//            fileChannel.write(byteBuffer);
//            byteBuffer.clear();
//        }
//
//        //fileChannel.write(byteBuffer);
//        fileChannel.close();
        //socketChannel.close();
    }
}
