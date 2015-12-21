/*
James Hahn

This program utilizes the Diamond-Square algorithm to create random terrain.  I chose this particular algorithm because
after considering several others (i.e. "Sparks", Hills, Particle Deposition), this one looked random, while at the same
time realistic without any artifical height placing by me.

It outputs a bitmap to the user in bitmap.png so they can see what the last execution resulted in.  I strongly encourage
the user to experiment with the SMOOTHNESS and LENGTH variables.

Possible changes:
- Scrolling terrain
- More precise color values for heights in the output image
*/

import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

public class DiamondSquare extends JFrame{
	//Set constants and global variables
	public static final int LENGTH = 9;
	public static final int MAX_SQUARES = (int) Math.pow(4, LENGTH);
	public static final int HEIGHT = (int) Math.pow(2, LENGTH)+20;
	public static final int WIDTH = (int) Math.pow(2, LENGTH)+20;
	public static final int SMOOTHNESS = 0; //how consistent the heights are throughout the terrain, higher = more consistent
	public static Random randomNum = new Random();
	public static double[][] heightmap = new double[(int) Math.pow(2, LENGTH)+1][(int) Math.pow(2, LENGTH)+1];

	//Set the JFrame properties
	public DiamondSquare(){
		setTitle("Diamond Square Bitmap");
		setSize(WIDTH, HEIGHT);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

	//Paint the heightmap to a JFrame for the user
	public void paint(Graphics g){
		for(int i = 0; i < heightmap.length; i++){
			for(int j = 0; j < heightmap.length; j++){
				float value = (float) heightmap[i][j];
				boolean grey = true;

				//Draw the intial grey values
				//Any grey value can be made by having r = g = b
				g.setColor(new Color(value, value, value));
				g.drawLine(i, j, i, j);

				//All heights are double values from 0.00-1.00
				//Low values ("Valleys"), 0.00-0.33, Green on heightmap
				//Middle values ("Hills"), 0.33-0.66, Greyish on heightmap
				//High values ("Mountains"), 0.66-1.00, White on heightmap
				if(heightmap[i][j] >= 0 && heightmap[i][j] < .33) g.setColor(new Color(44, 176, 55, 50));
				else if(heightmap[i][j] >= .33 && heightmap[i][j] < .66) g.setColor(new Color(133, 159, 168, 50));
				else g.setColor(new Color(255, 255, 255, 50));
				g.drawLine(i, j, i, j);
			}
		}
	}

	//Outputs the heightmap/bitmap to bitmap.png
	public static void outputBitmap(){
		try{
			File bitmap = new File("bitmap.png");
			bitmap.createNewFile();
			BufferedImage output = new BufferedImage(WIDTH-20, HEIGHT-20, BufferedImage.TYPE_INT_RGB);

			for(int i = 0; i < heightmap.length-1; i++){
				for(int j = 0; j < heightmap.length-1; j++){
					float mapValue = (float) heightmap[i][j];

					//I needed a way to combine the grey values and the colored values
					//into the .png output file, so I just averaged both values
					//and changed each pixel value in the image manually.
					float rAverage;
					float gAverage;
					float bAverage;
					if(mapValue >= 0 && mapValue < .33){
						rAverage = (mapValue*255 + 44)/2;
						gAverage = (mapValue*255 + 176)/2;
						bAverage = (mapValue*255 + 55)/2;
					} else if(mapValue >= .33 && mapValue < .66){
						rAverage = (mapValue*255 + 133)/2;
						gAverage = (mapValue*255 + 159)/2;
						bAverage = (mapValue*255 + 168)/2;
					} else{
						rAverage = (mapValue*255 + 255)/2;
						gAverage = (mapValue*255 + 255)/2;
						bAverage = (mapValue*255 + 255)/2;
					}
					Color col = new Color(rAverage/255, gAverage/255, bAverage/255, mapValue/255);
					output.setRGB(i, j, col.getRGB());
				}
			}

			ImageIO.write(output, "png", bitmap);
		} catch(IOException e2){
			e2.printStackTrace();
		}
	}

	//Optionally print the heightmap to the console
	public static void printHeightmap(){
		for(int i = 0; i < heightmap.length; i++){
			for(int j = 0; j < heightmap.length; j++){
				System.out.printf("%.4f ", heightmap[i][j]);
			}

			System.out.println();
		}
	}

	//Randomly decide where the displacement is added or subtracted
	public static int plusMinus(){
		double a = randomNum.nextDouble();
		if(a > .5) return 1;
		else return -1;
	}

	//Finds the average value for the midpoint of each square
	public static double averageValue(double value1, double value2, double value3, double value4){
		if(value1 == 0) return (value2+value3+value4)/3;
		else return (value1+value2+value3+value4)/4;
	}

	//Find the midpoint of each square
	public static int[] findMidpoint(int width, int height, int[] topLeft){
		return new int[] {topLeft[0] + width/2, topLeft[1] + height/2};
	}

	//Displaces the midpoint of the square by a certain value
	public static void displaceMidpoint(int[] center, int width, int height, int numIteration){
		double topLeftValue = 0;
		double topRightValue = 0;
		double bottomLeftValue = 0;
		double bottomRightValue = 0;

		if(center[1] != 0 && center[0] != 0) topLeftValue = heightmap[center[1] - height/2][center[0] - width/2];
		if(center[1] != 0 && center[0] != heightmap.length) topRightValue = heightmap[center[1] + height/2][center[0] - width/2];
		if(center[1] != heightmap.length && center[0] != 0) bottomLeftValue = heightmap[center[1] - height/2][center[0] + width/2];
		if(center[1] != heightmap.length && center[0] != heightmap.length) bottomRightValue = heightmap[center[1] + height/2][center[0] + width/2];

		if(heightmap[center[1]][center[0]] == 0) heightmap[center[1]][center[0]] = averageValue(topLeftValue, topRightValue, bottomLeftValue, bottomRightValue) + plusMinus()*(randomNum.nextDouble()/Math.pow(2, numIteration/2+SMOOTHNESS));
		if(heightmap[center[1]][center[0]] < 0) heightmap[center[1]][center[0]] = 0;
		else if(heightmap[center[1]][center[0]] > 1) heightmap[center[1]][center[0]] = 1;
	}

	//Implements the diamond step of the diamond-square algorithm
	public static void squareStep(int[] center, int width, int height, int numIteration){
		int left = center[0] - width/2;
		int top = center[1] - height/2;
		int right = center[0] + width/2;
		int bottom = center[1] + height/2;

		//Displaces the point left of the center
		if(heightmap[1][left] == 0){
			if(left == 0) heightmap[center[1]][left] = averageValue(0, heightmap[center[1] + height/2][left], heightmap[center[1] - height/2][left], heightmap[center[1]][center[0]]) + plusMinus()*randomNum.nextDouble()/Math.pow(2, numIteration);
			else heightmap[center[1]][left] = averageValue(heightmap[center[1]][left - width/2], heightmap[center[1] + height/2][left], heightmap[center[1] - height/2][left], heightmap[center[1]][center[0]]) + plusMinus()*randomNum.nextDouble()/Math.pow(2, numIteration/2+SMOOTHNESS);

			if(heightmap[center[1]][left] < 0) heightmap[center[1]][left] = 0;
			else if(heightmap[center[1]][left] > 1) heightmap[center[1]][left] = 1;
		}

		//Displaces theh point above the center
		if(heightmap[top][center[0]] == 0){
			if(top == 0) heightmap[top][center[0]] = averageValue(0, heightmap[center[1]][center[0]], heightmap[top][center[0] - width/2], heightmap[top][center[0] + width/2]) + plusMinus()*randomNum.nextDouble()/Math.pow(2, numIteration);
			else heightmap[top][center[0]] = averageValue(heightmap[top - height/2][center[0]], heightmap[center[1]][center[0]], heightmap[top][center[0] - width/2], heightmap[top][center[0] + width/2]) + plusMinus()*randomNum.nextDouble()/Math.pow(2, numIteration/2+SMOOTHNESS);

			if(heightmap[top][center[0]] < 0) heightmap[top][center[0]] = 0;
			else if(heightmap[top][center[0]] > 1) heightmap[top][center[0]] = 1;
		}

		//Displaces the point right of the center
		if(heightmap[center[1]][right] == 0){
			if(right == heightmap.length-1) heightmap[center[1]][right] = averageValue(0, heightmap[center[1] + height/2][right], heightmap[center[1] - height/2][right], heightmap[center[1]][center[0]]) + plusMinus()*randomNum.nextDouble()/Math.pow(2, numIteration);
			else heightmap[center[1]][right] = averageValue(heightmap[center[1]][right + width/2], heightmap[center[1] + height/2][right], heightmap[center[1] - height/2][right], heightmap[center[1]][center[0]]) + plusMinus()*randomNum.nextDouble()/Math.pow(2, numIteration/2+SMOOTHNESS);

			if(heightmap[center[1]][right] < 0) heightmap[center[1]][right] = 0;
			else if(heightmap[center[1]][right] > 1) heightmap[center[1]][right] = 1;
		}

		//Displaces the point below the center
		if(heightmap[bottom][center[0]] == 0){
			if(bottom == heightmap.length-1) heightmap[bottom][center[0]] = averageValue(0, heightmap[center[1]][center[0]], heightmap[bottom][center[0] - width/2], heightmap[bottom][center[0] + width/2]) + plusMinus()*randomNum.nextDouble()/Math.pow(2, numIteration);
			else heightmap[bottom][center[0]] = averageValue(heightmap[bottom - height/2][center[0]], heightmap[center[1]][center[0]], heightmap[bottom][center[0] - width/2], heightmap[bottom][center[0] + width/2]) + plusMinus()*randomNum.nextDouble()/Math.pow(2, numIteration/2+SMOOTHNESS);

			if(heightmap[bottom][center[0]] < 0) heightmap[bottom][center[0]] = 0;
			else if(heightmap[bottom][center[0]] > 1) heightmap[bottom][center[0]] = 1;
		}
	}

	//Implements the square step of the algorithm
	public static void transformTerrain(int[] topLeft, int[] topRight, int[] bottomLeft, int[] bottomRight, int numIteration){
		for(numIteration = numIteration; numIteration/2 < LENGTH; numIteration+=2){
			int numSquares = (int) Math.pow(2, numIteration+2)/4; //Number of squares in this iteration
			int squaresLength = (int) Math.pow(numSquares, .5); //How long one side of the square matrix is (in a 16x16, it'd be 16)
			int lengthRatio = (heightmap.length-1)/squaresLength; //Find the size of any given square
			int lengthRatio2 = (heightmap.length-1)/squaresLength / 2; //FInd how far the center of a square is away from its corners

			for(int i = 0; i < squaresLength; i++){
				for(int j = 0; j < squaresLength; j++){
					int[] center = findMidpoint(lengthRatio, lengthRatio, new int[] {topLeft[0] + lengthRatio*i, topLeft[1] + lengthRatio*j});
					displaceMidpoint(center, lengthRatio, lengthRatio, numIteration+1);
					squareStep(center, lengthRatio, lengthRatio, numIteration+1);
				}
			}
		}
	}

	public static void main(String[] args){
		//Set the initial corner values of the heightmap to be the same
		heightmap[0][0] = randomNum.nextDouble();
		heightmap[heightmap.length-1][0] = heightmap[0][0];
		heightmap[0][heightmap.length-1] = heightmap[0][0];
		heightmap[heightmap.length-1][heightmap.length-1] = heightmap[0][0];

		transformTerrain(new int[] {0, 0}, new int[] {0, heightmap.length}, new int[] {heightmap.length, 0}, new int[] {heightmap.length, heightmap.length}, 0);

		//new DiamondSquare();

		outputBitmap();
	}
}
