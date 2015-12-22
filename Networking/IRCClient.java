import java.net.*;
import java.io.*;
import java.util.*;

public class IRCClient{
	public static void main(String[] args){
		String hostname = args[0];
		int port = Integer.parseInt(args[1]);

		try{
			System.out.println("Connecting...");
			InetAddress address = InetAddress.getByName(hostname);
			DatagramSocket socket = new DatagramSocket();
			String username;
			Scanner sc = new Scanner(System.in);

			System.out.print("Enter a username: ");
			username = sc.nextLine();

			//MulticastSocket sock = new MulticastSocket(4446);
			//InetAddress group = InetAddress.getByName("203.0.113.0");
			//sock.joinGroup(group);

			String message = username + " connected!";
			byte[] buf = message.getBytes();
			DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
			socket.send(packet);

			while(address != null){
				System.out.print("Message: ");
				String line = sc.nextLine();
				message = username + ": " + line;
				if(line.equals("!QUIT")) break;
				buf = message.getBytes();
				packet = new DatagramPacket(buf, buf.length, address, port);

				socket.send(packet);
			}

			message = username + " disconnected!";
			buf = message.getBytes();
			packet = new DatagramPacket(buf, buf.length, address, port);
			socket.send(packet);

			System.out.print("Disconnecting...");
			//sock.leaveGroup(group);
			socket.close();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}
