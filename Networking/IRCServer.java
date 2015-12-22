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
			int portNumber = Integer.parseInt(args[0]);
			DatagramSocket socket = new DatagramSocket(portNumber);
			LocalDateTime time = LocalDateTime.now();

			System.out.println("Starting up server...");

			while(true){
				time = LocalDateTime.now();
				byte[] buf = new byte[256];
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				socket.receive(packet);

				String receivedString = new String(buf);
				System.out.println("<" + time.toString() + "> " + receivedString);
				if(receivedString.equals("Close Server")) break;

				// InetAddress clientAddress = packet.getAddress();
				// int port = packet.getPort();
				// packet = new DatagramPacket(buf, buf.length, clientAddress, port);
			}

			System.out.print("Closing down server...");
			socket.close();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}
