import java.io.*;
import java.util.*;
import java.awt.*;
import java.lang.*;
import javax.swing.*;

public class bitmaps extends Canvas{

	public void paint(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.CYAN);
		g2.drawLine(0, 0, -100, -100);
	}

	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("bitmap.bmp")));
		String readStrings = "";
		ArrayList<String> stringArray = new ArrayList<String>();
		ArrayList<Character> onesZeros = new ArrayList<Character>();

		do{
			readStrings = reader.readLine();
			if(readStrings == null) continue;
			System.out.println(readStrings);
			stringArray.add(readStrings);

			for(char ch: readStrings.toCharArray()){
				onesZeros.add(ch);
			}
		} while(readStrings != null);

		reader.close();
		System.out.println(stringArray);
		System.out.println(onesZeros);

		//Create the frame
		JFrame window = new JFrame("Bitmaps");

		//What happens when the window closes?
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setBackground(Color.BLACK);

		//Size the window
		int width = 500;
		int height = 500;
		//window.pack();  Makes it so it resizes itself based on what is drawn to the screen
		window.setSize(width, height);

		//Show the window
		window.setVisible(true);
	}
}
