import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

public class bitmaps extends JApplet{
	public void init(int[][] numArray){
		getContentPane().setBackground(Color.red);
		getBits();
	}

	// static int[][] numArray = new int[][] {
	// 	{1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
	// 	{1, 1, 0, 1, 0, 0, 0, 0, 0, 0},
	// 	{1, 1, 1, 1, 0, 0, 0, 0, 0, 0},
	// 	{1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
	// 	{1, 0, 0, 1, 0, 0, 0, 0, 0, 0},
	// 	{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
	// 	{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
	// 	{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
	// 	{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
	// 	{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
	// 	{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
	// 	{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
	// 	{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
	// 	{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
	// 	{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
	// 	{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
	// 	{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
	// 	{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
	// 	{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
	// 	{0, 0, 0, 0, 0, 0, 0, 0, 0, 0} };

	//This function reads from a bitmap file and stores the characters (0s and 1s) into arrayLists
	public static int[][] getBits(){
		File bitmap;
		Scanner reader;
		int[][] numArray = new int[20][10];
		char[][] charArray = new char[20][10];

		try{
			bitmap = new File("C:/Users/kingsman142/Desktop/Projects/bitmap.bmp");
			reader = new Scanner(bitmap);

			int row = 0;
			int column = 0;

			String readStrings = "";
			System.out.println("1");

			while(reader.hasNextLine()){
				System.out.println("2");
				readStrings = reader.nextLine();

				for(column = 0; column < readStrings.toCharArray().length; column++){
					System.out.println("3");
					charArray[row][column] = readStrings.toCharArray()[column];
					numArray[row][column] = Character.getNumericValue(readStrings.toCharArray()[column]);
					System.out.println("Success: " + charArray[row][column] + " at [ " + column + ", " + row + " ]");
					System.out.println("4");
				}

				for(column = column; column < 10; column++){
					numArray[row][column] = 0;
					charArray[row][column] = '0';
				}
				row++;
				System.out.println("5");
			}

			reader.close();
		} catch(Exception e){
			e.printStackTrace();
		}

		return numArray;
	}

	public void paint(Graphics g){
		super.paint(g);
		g.setColor(Color.black);

		//int[][] numArray = new int[20][10];
		int[][] numArray = getBits();

		// if(Arrays.equals(charArray, getBits())){
		// 	g.drawString("2", 125, 200);
		// 	numArray = getBits();
		// } else if(numArray == null){
		// 	g.drawString("4", 125, 200);
		// } else{
		// 	g.drawString("3", 125, 200);
		// 	numArray = getBits();
		// }
		int row = 0;
		int column = 0;
		int[][] ex = {{1, 2, 3}, {4, 5, 6}};

		for(row = 0; row < 20; row++){
			for(column = 0; column < 10; column++){
				g.drawString("1", 200, 50);
				g.drawString(Integer.toString(row), 150, 10 + row*10);
				g.drawString(Integer.toString(column), 125, 10 + column*10);
				g.drawString(Integer.toString(numArray[row][column]), column*10, 210 + row*10);
				if(numArray[row][column] == 1){
					g.setColor(Color.blue);
				} else if(numArray[row][column] == 0){
					g.setColor(Color.black);
				} else{
					g.setColor(Color.yellow);
				}

				g.fillRect(column*10, row*10, 10, 10);
			}
		}
	}

	public static void main(String[] args){
		int[][] numArray = getBits();
		//char[][] charArray = getBits();
		for(int row = 0; row < 20; row++){
			for(int column = 0; column < 10; column++){
				System.out.print(String.valueOf(numArray[row][column]) + " ");
				//System.out.print(charArray[row][column] + " ");
			}
			System.out.println("");
		}
	}
}
