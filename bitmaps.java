import java.util.Scanner;
import java.io.*;
import java.util.*;
import java.awt.*;

public class bitmaps{
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

		System.out.println(stringArray);
		System.out.println(onesZeros);

		reader.close();
	}
}
