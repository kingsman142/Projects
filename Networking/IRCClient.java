/*
Client file for an IRC in progress.  Stuck in a hard spot right now.  Takes 2 command-line arguments.

Optional project: Create an IRC bot
*/

import java.net.*;
import java.io.*;
import java.util.*;

public class IRCClient{
	public static void main(String[] args){
		String hostname = args[0];
		int port = Integer.parseInt(args[1]);

		try{
			// System.out.println("Connecting...");
			// InetAddress address = InetAddress.getByName(hostname);
			// DatagramSocket socket = new DatagramSocket();
			String username;
			Scanner sc = new Scanner(System.in);

			MulticastSocket sock = new MulticastSocket(port);
			System.out.println("1");
			InetAddress group = InetAddress.getByName("24.239.250.160");
			System.out.println("1.5");
			//sock.joinGroup(group);
			System.out.println("2");

			System.out.print("Enter a username: ");
			username = sc.nextLine();

			// String message = username + " connected!";
			// byte[] buf = message.getBytes();
			// DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
			// socket.send(packet);
			//
			// while(address != null){
			// 	System.out.print("Message: ");
			// 	String line = sc.nextLine();
			// 	message = username + ": " + line;
			// 	if(line.equals("!QUIT")) break;
			// 	buf = message.getBytes();
			// 	packet = new DatagramPacket(buf, buf.length, address, port);
			//
			// 	socket.send(packet);
			// }
			//
			// message = username + " disconnected!";
			// buf = message.getBytes();
			// packet = new DatagramPacket(buf, buf.length, address, port);
			// socket.send(packet);

			DatagramPacket packet;
			while(group != null){
				String message;
				System.out.print("Message: ");
				String line = sc.nextLine();
				message = username + ": " + line;
				byte[] buf = message.getBytes();
				if(line.equals("!QUIT")) break;

				packet = new DatagramPacket(buf, buf.length);
				sock.receive(packet);

				String received = new String(packet.getData());
				System.out.println("Received: " + received);
			}

			System.out.print("Disconnecting...");
			sock.leaveGroup(group);
			sock.close();
			//socket.close();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}
