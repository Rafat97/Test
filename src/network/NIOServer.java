/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.json.simple.JSONObject;

/**
 *
 * @author rafat
 */
public class NIOServer {

    public static void main(String[] args) throws IOException {

        InetAddress inetAddress = InetAddress.getLocalHost();
        System.out.println("IP Address:- " + inetAddress.getHostAddress());
        System.out.println("Host Name:- " + inetAddress.getHostName());

        // Selector: multiplexor of SelectableChannel objects
        Selector selector = Selector.open(); // selector is open here

        // ServerSocketChannel: selectable channel for stream-oriented listening sockets
        ServerSocketChannel crunchifySocket = ServerSocketChannel.open();
        InetSocketAddress crunchifyAddr = new InetSocketAddress(inetAddress.getHostAddress(), 1111);

        // Binds the channel's socket to a local address and configures the socket to listen for connections
        crunchifySocket.bind(crunchifyAddr);

        // Adjusts this channel's blocking mode.
        crunchifySocket.configureBlocking(false);

        int ops = crunchifySocket.validOps();
        SelectionKey selectKy = crunchifySocket.register(selector, ops, null);

        // Infinite loop..
        // Keep server running
        while (true) {

            log("i'm a server and i'm waiting for new connection and buffer select...");
            // Selects a set of keys whose corresponding channels are ready for I/O operations
            selector.select();

            // token representing the registration of a SelectableChannel with a Selector
            Set<SelectionKey> crunchifyKeys = selector.selectedKeys();
            Iterator<SelectionKey> crunchifyIterator = crunchifyKeys.iterator();

            while (crunchifyIterator.hasNext()) {
                SelectionKey myKey = crunchifyIterator.next();

                // Tests whether this key's channel is ready to accept a new socket connection
                if (myKey.isAcceptable()) {
                    SocketChannel crunchifyClient = crunchifySocket.accept();

                    // Adjusts this channel's blocking mode to false
                    crunchifyClient.configureBlocking(false);

                    // Operation-set bit for read operations
                    crunchifyClient.register(selector, SelectionKey.OP_READ);
                    log("Connection Accepted: " + crunchifyClient.getLocalAddress() + "\n");

                    // Tests whether this key's channel is ready for reading
                } else if (myKey.isReadable()) { 

                    SocketChannel crunchifyClient = (SocketChannel) myKey.channel();
                    ByteBuffer crunchifyBuffer = ByteBuffer.allocate(128);
                    crunchifyClient.read(crunchifyBuffer);
                    String result = new String(crunchifyBuffer.array()).trim();
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);

                    String originalJson = result;
//                    JsonNode tree = objectMapper.readTree(originalJson);
//                    String formattedJson = objectMapper.writeValueAsString(tree);
//                    log("Message received: " + formattedJson);
                    log("Message received: " + originalJson);

//					if ( result.equals("finish")) {
//						//crunchifyClient.close();
//						log("\nIt's time to close connection as we got last company name 'finish'");
//						log("\nServer will keep running. Try running client again to establish new connection");
//                                               
//                                                byte[] message = new String("Done").getBytes();
//                                                ByteBuffer responceSend = ByteBuffer.wrap(message);
//                                                crunchifyClient.write(responceSend);
//                                                responceSend.clear();
//                                                
//                                                crunchifyClient.close();
//                                        }
                    //send response server to client
                    JSONObject root = new JSONObject();
                    root.put("response_type", "success");
                    root.put("response_code", 200);
                    root.put("message", "Thank you to send a data into server");
                    byte[] message = new String(root.toString()).getBytes();
                    ByteBuffer responceSend = ByteBuffer.wrap(message);
                    crunchifyClient.write(responceSend);
                    responceSend.clear();

                    crunchifyClient.close();

                }
                crunchifyIterator.remove();
            }
        }
    }

    private static void log(String str) {
        System.out.println(str);
    }
}
