/*
James Hahn

This program takes input of any image of any size.
It then proceeds to:
	(1) Convert the image to black-white greyscale (only 1s and 0s) - stores in bitmap.bmp
	(2) Convert the image to complete greyscale (doubles from 0.00-255.00) - stores in greyValues.bmp

At run-time, the user enters the filename and whether they want black and white (B) which is stored in BinaryOutput.png
or whether they want greyscale (G) which is stored in GreyOutput.png.

PNG files were used as the output file type because they are lossless, therefore the quality will be
higher than that of a JPG file.

THIS PROGRAM DOES NOT COMPENSATE FOR THE USER IF THEY DO NOT ENTER THE FILE EXTENSION OR THEY DO NOT ENTER
A FILENAME THAT EXISTS!
*/

import java.io.*;
import javax.imageio.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

public class bitmaps extends JApplet{

	//These two variables are used so the program can greyscale/binary(ize) any image...
	//regardless of the filename and then the user decides if they want a greyscale (G)...
	//or binary (B) image (this is stored in choiceString).
	public static String[] nameString = {"leaf.jpg"};
	public static String[] choiceString = {"G"};

	public void init(int[][] numArray){
		//Sets the default background color
		getContentPane().setBackground(Color.red);
	}

	public static void convertImage(File image){
		try{
			//Takes in image (Buffered Image).
			//Also creates several other variables for storying crucial information
			BufferedImage img = ImageIO.read(image);
			PrintWriter writer = new PrintWriter("bitmap.bmp");
			PrintWriter writerG = new PrintWriter("greyValues.bmp");
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
				//Writes to the file what the bitline is
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

	//Creates an image file and outputs it to GreyOutput.png for the user to use in the future
	//GREYSCALE CONVERSION (0.0-255.0)
	public static void createOutput(File image) throws Exception{
		BufferedImage img = ImageIO.read(image);
		int width = img.getWidth();
		int height = img.getHeight();
		BufferedImage img2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		double[] greys = getGreys();
		int counter = 0;

		for(int row = 0; row < height; row++){
			for(int column = 0; column < width; column++){
				float value = (float) (greys[counter]/255.0);
				int valueInt = (int) greys[counter];
				Color col = new Color(valueInt, valueInt, valueInt);
				img2.setRGB(column, row, col.getRGB());
				counter++;
			}
		}

		ImageIO.write(img2, "png", new File("GreyOutput.png"));
	}

	//Creates an image file and outputs it to BinaryOutput.png for the user to use in the future
	//BINARY CONVERSION (1s AND 0S)
	public static void createOutputBW(File image) throws Exception{
		BufferedImage img = ImageIO.read(image);
		int width = img.getWidth();
		int height = img.getHeight();
		BufferedImage img2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		int[][] numArray = getBits();

		for(int row = 0; row < height; row++){
			for(int column = 0; column < width; column++){
				int value = 0;
				if(numArray[row][column] == 1){
					value = 255;
				} else if(numArray[row][column] == 0){
					value = 0;
				}
				Color col = new Color(value, value, value);
				img2.setRGB(column, row, col.getRGB());
			}
		}

		ImageIO.write(img2, "png", new File("BinaryOutput.png"));
	}

	//Returns all the grey values from greyValues.bmp
	public static double[] getGreys(){
		try{
			//Takes in the image solely to find its height and width on an image by image basis...
			//so I don't have to manually resize greyArray every time
			BufferedImage img = ImageIO.read(new File(nameString[0]));
			int width = img.getWidth();
			int height = img.getHeight();
			double[] greyArray = new double[width*height];

			//Sets up greyValues.bmp so the program can read the values from it and store them...
			//in greyArray
			File bitmapG = new File("greyValues.bmp");
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
			BufferedImage img = ImageIO.read(new File(nameString[0]));
			int width = img.getWidth();
			int height = img.getHeight();
			int[][] numArray = new int[height][width];

			//Sets up bitmap.bmp so the program can read from it and store the values in numArray
			File bitmap = new File("bitmap.bmp");
			Scanner reader = new Scanner(bitmap);
			int row = 0;
			String readStrings = "";

			while(reader.hasNextLine()){
				readStrings = reader.nextLine();

				for(int column = 0; column < readStrings.toCharArray().length; column++){
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

	/*
	paint() is useless unless you use the appletviewer, which there is little reason to utilize.
	However, if you want to experiment with the appletviewer, you are free to uncomment this function.
	To use the appletviewer with this program, use "appletviewer bitmaps.html" in the command prompt.
	*/

	// public void paint(Graphics g){
	// 	super.paint(g);
	// 	g.setColor(Color.black);
	//
	// 	//Grab the two arrays...
	// 	//(1) numArray - only 1s and 0s
	// 	//(2) greys - actual grey values in double type from 0.00 to 255.00
	// 	int[][] numArray = getBits();
	// 	double[] greys = getGreys();
	//
	// 	int row = 0;
	// 	int column = 0;
	// 	int counter = 0;
	//
	// 	for(row = 0; row < numArray.length; row++){
	// 		for(column = 0; column < numArray[row].length; column++){
	// 			// Change choiceString[0] to either "B" or "G" to switch from...
	// 			// Binary to Greyscale imaging
	// 			if(choiceString[0].equals("B")){
	// 				if(numArray[row][column] == 1){
	// 					g.setColor(Color.white);
	// 				} else if(numArray[row][column] == 0){
	// 					g.setColor(Color.black);
	// 				} else{
	// 					g.setColor(Color.yellow);
	// 				}
	// 			} else if(choiceString[0].equals("G")){
	// 				int value = (int) greys[counter];
	// 				g.setColor(new Color(value, value, value));
	// 			}
	//
	// 			g.fillRect(column, row, 1, 1);
	// 			counter++;
	// 		}
	// 	}
	// }

	public static void main(String[] args) throws Exception{
		//Ask the user for the filename and which type of conversion they would like to make.
		//Then the image is coverted with convertImage().
		//And finally the outputted image is created depending on the conversion the user decided on.
		System.out.print("Filename (with extension): ");
		Scanner sc = new Scanner(System.in);
		nameString[0] = sc.next();
		System.out.print("Black and white (B) or Greyscale (G): ");
		choiceString[0] = sc.next();

		convertImage(new File(nameString[0]));

		if(choiceString[0].equals("B")){
			createOutputBW(new File(nameString[0]));
			System.out.println("Image stored in BinaryOutput.png!");
		} else if(choiceString[0].equals("G")){
			createOutput(new File(nameString[0]));
			System.out.println("Image stored in GreyOutput.png!");
		} else{
			System.out.println("Error! Exiting!");
			System.exit(3);
		}
	}
}
