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
			int width = img.getWidth();
			int height = img.getHeight();
			int[] bitLine = new int[width];
			System.out.println("Width: " + width);
			System.out.println("Height: " + height);

			for(int row = 0; row < height; row++){
				for(int column = 0; column < width; column++){
					Color color = new Color(img.getRGB(column, row));
					double r = color.getRed();
					double g = color.getGreen();
					double b = color.getBlue();
					double result = .3*r + .59*g + .11*b;
					//System.out.println("Result: " + result + ", " + r + ", " + g + ", " + b);
					int bOrW = 0;
					if(result < 127.5 && result >= 0) {
						bOrW = 0;
					} else if(result >= 127.5 && result <= 255){
						bOrW = 1;
					} else{
						bOrW = 2;
					}
					bitLine[column] = bOrW;
				}
				String line = "";
				for(int i: bitLine){
					line += Integer.toString(i);
				}
				System.out.println(line);

				writer.println(line);
			}

			writer.close();
		} catch(Exception e){
			e.printStackTrace();
		} finally{
		}
	}

	//This function reads from a bitmap file and stores the characters (0s and 1s) into arrayLists
	public static int[][] getBits(){
		int[][] numArray = new int[200][100];

		try{
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
		} catch(Exception e){
			e.printStackTrace();
		}

		return numArray;
	}

	public void paint(Graphics g){
		super.paint(g);
		g.setColor(Color.black);

		int[][] numArray = getBits();

		int row = 0;
		int column = 0;

		for(row = 0; row < numArray.length; row++){
			for(column = 0; column < numArray[row].length; column++){
				if(numArray[row][column] == 1){
					g.setColor(Color.white);
				} else if(numArray[row][column] == 0){
					g.setColor(Color.black);
				} else{
					g.setColor(Color.yellow);
				}

				g.fillRect(column, row, 1, 1);
			}
		}
	}

	public static void main(String[] args){
		//convertImage(new File("C:/Users/kingsman142/Desktop/Projects/face.png"));
		convertImage(new File("http://www.absyx.fr/sites/default/files/imagecache/Preset-4-3/imagefield/test-page/image-focus.jpg"));
	}
}
