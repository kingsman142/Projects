import java.io.*;
import java.util.*;
import java.awt.*;

public class bitmaps{
	public void paintCOmponent(Graphics g){
		super.paintComponent(g);

		g.setColor(Color.BLACK);
		g.drawLine(1, 2, 1, 2);
	}

	public static void main(String[] args) throws IOException, NoSuchElementException {
		File bitmapFile = new File("bitmap.bmp");
		Scanner reader = new Scanner(bitmapFile);

		int numLines = 0;

		String readStrings = "";
		ArrayList<String> stringArray = new ArrayList<String>();
		ArrayList<Character> onesZeros = new ArrayList<Character>();

		do{
			readStrings = reader.nextLine();
			System.out.println(readStrings);
			stringArray.add(readStrings);
			numLines++;

			for(char ch: readStrings.toCharArray()){
				onesZeros.add(ch);
			}
		} while(reader.hasNextLine());

		System.out.println(onesZeros);

		reader.close();

		JFrame window = new JFrame();
		Panel p = new Panel();
		window.setTitle("bitmaps");
		window.setSize(500, 500);
		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.add(p);
	}
}
