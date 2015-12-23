/*
Server file for an IRC in progress.  Takes 1 command-line argument.
*/

import java.io.*;
import java.net.*;
import java.util.*;
import java.time.*;

public class IRCServer extends Thread{
	public static void getSiteCode(URL website){
		try{
			Scanner sc = new Scanner(website.openStream());
			while(sc.hasNextLine()){
				System.out.println(sc.nextLine());
			}
			sc.close();
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void main(String[] args){
		try{
			int port = Integer.parseInt(args[0]);
			DatagramSocket socket = new DatagramSocket(port);
			LocalDateTime time = LocalDateTime.now();

			System.out.println("Starting up server...");
			InetAddress groupAddress = InetAddress.getByName("24.239.250.160");

			while(true){
				time = LocalDateTime.now();
				byte[] buf = new byte[256];
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				socket.receive(packet);

				String receivedString = new String(buf);
				System.out.println("<" + time.toString() + "> " + receivedString);
				if(receivedString.equals("Close Server")) break;

				packet = new DatagramPacket(buf, buf.length, groupAddress, port);
			}

			System.out.print("Closing down server...");
			socket.close();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}
