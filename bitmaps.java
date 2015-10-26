import java.io.*;
import javax.imageio.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

public class bitmaps extends JApplet{

	public void init(int[][] numArray){
		//Sets the default background color
		getContentPane().setBackground(Color.red);
	}

	public static void convertImage(File image){
		try{
			//Takes in image (Buffered Image).
			//Also creates several other variables for storying crucial information
			BufferedImage img = ImageIO.read(image);
			PrintWriter writer = new PrintWriter("C:/Users/kingsman142/Desktop/Projects/bitmap.bmp");
			PrintWriter writerG = new PrintWriter("C:/Users/kingsman142/Desktop/Projects/greyValues.bmp");
			int width = img.getWidth();
			int height = img.getHeight();
			int[] bitLine = new int[width];
			double[][] greys = new double[height][width];

			//Loops through entire pixel in the image
			for(int row = 0; row < height; row++){
				for(int column = 0; column < width; column++){
					//Grabs color and r, g, b values of every pixel and calculates greyscale
					Color color = new Color(img.getRGB(column, row));
					double r = color.getRed();
					double g = color.getGreen();
					double b = color.getBlue();
					double result = .3*r + .59*g + .11*b;

					//Determines if a pixel should be black or white in that specific area of greyscaling
					int bOrW = 0;
					if(result < 127.5 && result >= 0) {
						bOrW = 0;
					} else if(result >= 127.5 && result <= 255){
						bOrW = 1;
					}
					bitLine[column] = bOrW;

					//Prints out the double value of the greyscale result
					writerG.println(Double.toString(result));
				}
				//For debugging reasons, executes this so I can see the image in 1s and 0s
				String line = "";
				for(int i: bitLine){
					line += Integer.toString(i);
				}
				//System.out.println(line);

				writer.println(line);
			}

			writer.close();
			writerG.close();
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	public static double[] getGreys(){
		try{
			//Takes in the image solely to find its height and width on an image by image basis...
			//so I don't have to manually resize greyArray every time
			BufferedImage img = ImageIO.read(new File("leaf.jpg"));
			int width = img.getWidth();
			int height = img.getHeight();
			double[] greyArray = new double[width*height];

			//Sets up greyValues.bmp so the program can read the values from it and store them...
			//in greyArray
			File bitmapG = new File("C:/Users/kingsman142/Desktop/Projects/greyValues.bmp");
			Scanner readerG = new Scanner(bitmapG);
			int row = 0;
			int column = 0;
			String readStrings = "";

			while(readerG.hasNextLine()){
				readStrings = readerG.nextLine();
				greyArray[row] = Double.parseDouble(readStrings);

				row++;
			}

			readerG.close();
			return greyArray;
		} catch(Exception e){
			e.printStackTrace();
		}

		return null;
	}

	//This function reads from a bitmap file and stores the characters (0s and 1s) into arrayLists
	public static int[][] getBits(){
		try{
			//Takes in the image solely to find its height and width on an image by image basis...
			//so I don't have to manually resize numArray every time
			BufferedImage img = ImageIO.read(new File("leaf.jpg"));
			int width = img.getWidth();
			int height = img.getHeight();
			int[][] numArray = new int[height][width];

			//Sets up bitmap.bmp so the program can read from it and store the values in numArray
			File bitmap = new File("C:/Users/kingsman142/Desktop/Projects/bitmap.bmp");
			Scanner reader = new Scanner(bitmap);
			int row = 0;
			int column = 0;
			String readStrings = "";

			while(reader.hasNextLine()){
				readStrings = reader.nextLine();

				for(column = 0; column < readStrings.toCharArray().length; column++){
					numArray[row][column] = Character.getNumericValue(readStrings.toCharArray()[column]);
				}

				row++;
			}

			reader.close();
			return numArray;
		} catch(Exception e){
			e.printStackTrace();
		}

		return null;
	}

	public void paint(Graphics g){
		super.paint(g);
		g.setColor(Color.black);

		//Grab the two arrays...
		//(1) numArray - only 1s and 0s
		//(2) greys - actual grey values in double type from 0.00 to 255.00
		int[][] numArray = getBits();
		double[] greys = getGreys();

		int row = 0;
		int column = 0;
		int counter = 0;

		for(row = 0; row < numArray.length; row++){
			for(column = 0; column < numArray[row].length; column++){
				// UNCOMMENT THIS TO MAKE THE VALUES ONLY BLACK AND WHITE
				// if(numArray[row][column] == 1){
				// 	g.setColor(Color.white);
				// } else if(numArray[row][column] == 0){
				// 	g.setColor(Color.black);
				// } else{
				// 	g.setColor(Color.yellow);
				// }

				// BELOW 2 LINES CONVERT IT TO A COMPLETE GREYSCALE
				int value = (int) greys[counter];
				g.setColor(new Color(value, value, value));

				g.fillRect(column, row, 1, 1);
				counter++;
			}
		}
	}

	public static void main(String[] args){
		//Take this image and convert it to greyscale values and 1s and 0s.
		//Store the values in greyValues.bmp and bitmap.bmp
		//convertImage(new File("face.png"));
		convertImage(new File("leaf.jpg"));
	}
}
