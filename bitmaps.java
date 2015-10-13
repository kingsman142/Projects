import java.io.*;
import java.util.*;
import java.awt.*;

public class bitmaps{
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
	}
}
