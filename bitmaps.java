import java.io.*;
import javax.imageio.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

public class bitmaps extends JApplet{

	public void init(int[][] numArray){
		getContentPane().setBackground(Color.red);
	}

	public static void convertImage(File image){
		try{
			BufferedImage img = ImageIO.read(image);
			PrintWriter writer = new PrintWriter("C:/Users/kingsman142/Desktop/Projects/bitmap.bmp");
			PrintWriter writerG = new PrintWriter("C:/Users/kingsman142/Desktop/Projects/greyValues.bmp");
			int width = img.getWidth();
			int height = img.getHeight();
			int[] bitLine = new int[width];
			double[][] greys = new double[height][width];

			for(int row = 0; row < height; row++){
				for(int column = 0; column < width; column++){
					Color color = new Color(img.getRGB(column, row));
					double r = color.getRed();
					double g = color.getGreen();
					double b = color.getBlue();
					double result = .3*r + .59*g + .11*b;
					int bOrW = 0;
					if(result < 127.5 && result >= 0) {
						bOrW = 0;
					} else if(result >= 127.5 && result <= 255){
						bOrW = 1;
					}
					bitLine[column] = bOrW;
					writerG.println(Double.toString(result));
				}
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
			BufferedImage img = ImageIO.read(new File("leaf.jpg"));
			int width = img.getWidth();
			int height = img.getHeight();
			double[] greyArray = new double[width*height];

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
			BufferedImage img = ImageIO.read(new File("leaf.jpg"));
			int width = img.getWidth();
			int height = img.getHeight();
			int[][] numArray = new int[height][width];

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
		//convertImage(new File("face.png"));
		convertImage(new File("leaf.jpg"));
	}
}
