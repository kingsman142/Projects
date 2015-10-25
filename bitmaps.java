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
			ArrayList<String> stringArray = new ArrayList<String>();
			ArrayList<Character> onesZeros = new ArrayList<Character>();
			ArrayList<Integer> numArray = new ArrayList<Integer>();
			int[][] numArray2 = new int[10][20];

			while(reader.hasNextLine()){
				readStrings = reader.nextLine();
				System.out.println(readStrings);
				stringArray.add(readStrings);
				numLines++;

				// for(char ch: readStrings.toCharArray()){
				// 	int a = Character.getNumericValue(ch);
				// 	onesZeros.add(ch);
				// 	numArray.add(a);
				// 	System.out.print(a + " ");
				// }

				for(column = 0; column < readStrings.toCharArray().length; column++){
					numArray2[row][column] = Character.getNumericValue(readStrings.toCharArray()[column]);
				}

				row++;
			}

			reader.close();

			ArrayList[] arrayArray = new ArrayList[3];
			arrayArray[0] = stringArray;
			arrayArray[1] = onesZeros;
			arrayArray[2] = numArray;

			return numArray2;
		} catch(Exception e){

		} finally{

		}

		return null;
	}

	public void paint(Graphics g){
		super.paint(g);
		g.setColor(Color.black);

		g.drawString("begin", 25, 25);

		ArrayList[] arrayArray = new ArrayList[2];
		//arrayArray = getBits();
		int[][] numArray2 = getBits();


		//ArrayList<String> bitLines = arrayArray[0];
		//ArrayList<Character> onesZeros = arrayArray[1];
		ArrayList<Integer> numArray = new ArrayList<Integer>();
		// for(Integer a: arrayArray[2]){
		// 	numArray.add(a);
		// }

		// for(int i = 0; i < arrayArray[2].size(); i++){
		// 	g.drawString("2", 50, 50);
		// 	g.drawString("" + arrayArray[2].get(i), i*10, 100);
		// }
		//g.drawString("Size: " + arrayArray[2].size(), 50, 50);

		for(int column = 0; column < numArray2.length; column++){
			for(int row = 0; row < numArray2[row].length; row++){
				g.drawString("" + numArray2[row][column], row*10, column*25);
			}
		}

		int x = 0;
		g.drawString("1", 25, 25);

		// for(int bit: numArray){
		// 	g.drawString("" + bit, x*30, 100);
		// 	if(bit == 0){
		// 		g.setColor(Color.white);
		// 	} else if(bit == 1){
		// 		g.setColor(Color.black);
		// 	} else{
		// 		g.setColor(Color.yellow);
		// 	}
		// 	g.fillRect(x*10, 0, 10, 10);
		// 	x++;
		// }
	}

	public static void main(String[] args) throws FileNotFoundException{
		/*JFrame window = new JFrame("bitmaps");
		window.setSize(100, 200);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);*/
		// ArrayList[] arrayArray = new ArrayList[3];
		// arrayArray = getBits();
		// ArrayList<Integer> numArray = arrayArray[2];
		// for(int a: numArray){
		// 	System.out.println(a);
		// }

		int[][] numArray2 = getBits();
		for(int row = 0; row < numArray2.length; row++){
			for(int column = 0; column < numArray2[row].length; column++){
				System.out.print(numArray2[row][column] + " ");
			}
			System.out.println("");
		}
	}
}
