/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 *
 * @author rafat
 */
public class NIOClient {

    public static void main(String[] args) throws IOException, InterruptedException {
//1111
        InetSocketAddress crunchifyAddr = new InetSocketAddress("192.168.123.103",28280 );
        SocketChannel crunchifyClient = SocketChannel.open(crunchifyAddr);

		ArrayList<String> companyDetails = new ArrayList<String>();
 
		// create a ArrayList with companyName list
		companyDetails.add("data asdasdfgdgdfgdfgdfgdfgasdjasidjiasdiaaaaaaaaaaaasddddddddddddddhdojasdinasofpkasnfpishnfpoinasdpfo = Test android , Time="+System.currentTimeMillis());
//               companyDetails.add("finish");
 
		for (String companyName : companyDetails) {
 
			byte[] message = new String(companyName).getBytes();
			ByteBuffer buffer = ByteBuffer.wrap(message);
			crunchifyClient.write(buffer);
 
			log("sending: " + companyName);
			buffer.clear();
 
			// wait for 2 seconds before sending next message
			Thread.sleep(2000);
		}
//                


//        Path path = Paths.get("C:\\Users\\rafat\\OneDrive\\Desktop\\heart.csv");
//        FileChannel fileChannel = FileChannel.open(path);
//        ByteBuffer buffer = ByteBuffer.allocate(1024);
//        while (fileChannel.read(buffer) > 0) {            
//            buffer.flip();
//            crunchifyClient.write(buffer);
//            buffer.clear();
//        }

        ByteBuffer crunchifyBuffer = ByteBuffer.allocate(128);
        crunchifyClient.read(crunchifyBuffer);
        String result = new String(crunchifyBuffer.array()).trim();
        System.out.println("Respose = " + result);
        crunchifyClient.close();
    }

    private static void log(String str) {
        System.out.println(str);
    }
}
