import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

public class bitmaps extends JApplet{
	public void init(){
		getContentPane().setBackground(Color.red);
	}

	//This function reads from a bitmap file and stores the characters (0s and 1s) into arrayLists
	public static int[][] getBits(){
		File bitmap;
		Scanner reader;

		try{
			bitmap = new File("C:/Users/kingsman142/Desktop/Projects/bitmap.bmp");
			reader = new Scanner(bitmap);

			int numLines = 0;
			int row = 0;
			int column = 0;

			String readStrings = "";
			int[][] numArray2 = new int[20][10];

			while(reader.hasNextLine()){
				readStrings = reader.nextLine();
				System.out.println(readStrings);
				numLines++;

				for(column = 0; column < readStrings.toCharArray().length; column++){
					numArray2[row][column] = Character.getNumericValue(readStrings.toCharArray()[column]);
				}

				row++;
			}

			reader.close();

			return numArray2;
		} catch(Exception e){

		}

		return null;
	}

	public void paint(Graphics g){
		super.paint(g);
		g.setColor(Color.black);

		g.drawString("begin", 25, 25);

		int[][] numArray2 = getBits();

		for(int column = 0; column < numArray2.length; column++){
			for(int row = 0; row < numArray2[row].length; row++){
				g.drawString("" + numArray2[row][column], row*10, column*25);
			}
		}

		int x = 0;
		g.drawString("1", 25, 25);
	}

	public static void main(String[] args) throws FileNotFoundException{
		int[][] numArray2 = getBits();
		for(int row = 0; row < numArray2.length; row++){
			for(int column = 0; column < numArray2[row].length; column++){
				System.out.print(numArray2[row][column] + " ");
			}
			System.out.println("");
		}
	}
}
