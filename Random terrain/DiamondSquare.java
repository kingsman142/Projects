import java.util.*;
import java.awt.*;
import javax.swing.*;

public class DiamondSquare extends JFrame{
	//Set constants and global variables
	public static final int LENGTH = 8;
	public static final int MAX_SQUARES = (int) Math.pow(4, LENGTH);
	public static final int HEIGHT = (int) Math.pow(2, LENGTH)+1;
	public static final int WIDTH = (int) Math.pow(2, LENGTH)+1;
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
				g.setColor(new Color(value, value, value));
				g.drawLine(i, j, i, j);
				if(heightmap[i][j] >= 0 && heightmap[i][j] < .33) g.setColor(new Color(44, 176, 55, 50));
				else if(heightmap[i][j] >= .33 && heightmap[i][j] < .66) g.setColor(new Color(133, 159, 168, 50));
				else g.setColor(new Color(255, 255, 255, 50));
				g.drawLine(i, j, i, j);
			}
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

	//Check to see if the heightmap is full
	public static boolean fullHeightmap(){
		for(int i = 0; i < heightmap.length; i++){
			for(int j = 0; j < heightmap.length; j++){
				if(heightmap[i][j] == 0.0) return false;
			}
		}
		return true;
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

		if(heightmap[center[1]][center[0]] == 0) heightmap[center[1]][center[0]] = averageValue(topLeftValue, topRightValue, bottomLeftValue, bottomRightValue) + plusMinus()*(randomNum.nextDouble()/Math.pow(2, numIteration/2));
		if(heightmap[center[1]][center[0]] < 0) heightmap[center[1]][center[0]] = 0;
		else if(heightmap[center[1]][center[0]] > 1) heightmap[center[1]][center[0]] = 1;
	}

	//Implements the diamond step of the diamond-square algorithm
	public static void squareStep(int[] center, int width, int height, int numIteration){
		int left = center[0] - width/2;
		int top = center[1] - height/2;
		int right = center[0] + width/2;
		int bottom = center[1] + height/2;

		if(heightmap[1][left] == 0){
			if(left == 0) heightmap[center[1]][left] = averageValue(0, heightmap[center[1] + height/2][left], heightmap[center[1] - height/2][left], heightmap[center[1]][center[0]]) + plusMinus()*randomNum.nextDouble()/Math.pow(2, numIteration);
			else heightmap[center[1]][left] = averageValue(heightmap[center[1]][left - width/2], heightmap[center[1] + height/2][left], heightmap[center[1] - height/2][left], heightmap[center[1]][center[0]]) + plusMinus()*randomNum.nextDouble()/Math.pow(2, numIteration/2);

			if(heightmap[center[1]][left] < 0) heightmap[center[1]][left] = 0;
			else if(heightmap[center[1]][left] > 1) heightmap[center[1]][left] = 1;
		}

		if(heightmap[top][center[0]] == 0){
			if(top == 0) heightmap[top][center[0]] = averageValue(0, heightmap[center[1]][center[0]], heightmap[top][center[0] - width/2], heightmap[top][center[0] + width/2]) + plusMinus()*randomNum.nextDouble()/Math.pow(2, numIteration);
			else heightmap[top][center[0]] = averageValue(heightmap[top - height/2][center[0]], heightmap[center[1]][center[0]], heightmap[top][center[0] - width/2], heightmap[top][center[0] + width/2]) + plusMinus()*randomNum.nextDouble()/Math.pow(2, numIteration/2);

			if(heightmap[top][center[0]] < 0) heightmap[top][center[0]] = 0;
			else if(heightmap[top][center[0]] > 1) heightmap[top][center[0]] = 1;
		}

		if(heightmap[center[1]][right] == 0){
			if(right == heightmap.length-1) heightmap[center[1]][right] = averageValue(0, heightmap[center[1] + height/2][right], heightmap[center[1] - height/2][right], heightmap[center[1]][center[0]]) + plusMinus()*randomNum.nextDouble()/Math.pow(2, numIteration);
			else heightmap[center[1]][right] = averageValue(heightmap[center[1]][right + width/2], heightmap[center[1] + height/2][right], heightmap[center[1] - height/2][right], heightmap[center[1]][center[0]]) + plusMinus()*randomNum.nextDouble()/Math.pow(2, numIteration/2);

			if(heightmap[center[1]][right] < 0) heightmap[center[1]][right] = 0;
			else if(heightmap[center[1]][right] > 1) heightmap[center[1]][right] = 1;
		}

		if(heightmap[bottom][center[0]] == 0){
			if(bottom == heightmap.length-1) heightmap[bottom][center[0]] = averageValue(0, heightmap[center[1]][center[0]], heightmap[bottom][center[0] - width/2], heightmap[bottom][center[0] + width/2]) + plusMinus()*randomNum.nextDouble()/Math.pow(2, numIteration);
			else heightmap[bottom][center[0]] = averageValue(heightmap[bottom - height/2][center[0]], heightmap[center[1]][center[0]], heightmap[bottom][center[0] - width/2], heightmap[bottom][center[0] + width/2]) + plusMinus()*randomNum.nextDouble()/Math.pow(2, numIteration/2);

			if(heightmap[bottom][center[0]] < 0) heightmap[bottom][center[0]] = 0;
			else if(heightmap[bottom][center[0]] > 1) heightmap[bottom][center[0]] = 1;
		}
	}

	//Implements the square step of the algorithm
	public static void transformTerrain(int[] topLeft, int[] topRight, int[] bottomLeft, int[] bottomRight, int numIteration){
		int currentLength = (int) Math.pow(2, numIteration);
		int numSquares = (int) Math.pow(2, numIteration+2)/4;
		//if(!fullHeightmap()) System.out.println("HEIGHT MAP NOT FULL");
		if(currentLength <= MAX_SQUARES) System.out.println("NOT ENOUGH SQUARES, max squares: " + MAX_SQUARES + " , current squares: " + currentLength);
		if(!fullHeightmap() && numSquares <= MAX_SQUARES){
			System.out.println("Executing");
			int squaresLength = (int) Math.pow(numSquares, .5);
			int lengthRatio = (heightmap.length-1)/squaresLength;
			int lengthRatio2 = (heightmap.length-1)/squaresLength / 2;
			System.out.println("NumSquares: " + numSquares + " , SquaresLength: " + squaresLength);
			int width = topRight[1] - topLeft[1];
			int height = bottomLeft[0] - topLeft[0];

			for(int i = 0; i < squaresLength; i++){
				for(int j = 0; j < squaresLength; j++){
					int[] center = findMidpoint(lengthRatio, lengthRatio, new int[] {topLeft[0] + lengthRatio*i, topLeft[1] + lengthRatio*j});
					displaceMidpoint(center, lengthRatio, lengthRatio, numIteration+1);
					squareStep(center, lengthRatio, lengthRatio, numIteration+1);
				}
			}

			for(int i = 0; i < squaresLength; i++){
				for(int j = 0; j < squaresLength; j++){
					transformTerrain(topLeft, topRight, bottomLeft, bottomRight, numIteration+2);
					//System.out.println("Run");
					//System.out.println("i: " + i + " , j: " + j);
				}
			}
		} else{
			return;
		}
	}

	public static void main(String[] args){
		//Set the initial corner values of the heightmap to be the same
		heightmap[0][0] = randomNum.nextDouble();
		heightmap[heightmap.length-1][0] = heightmap[0][0];
		heightmap[0][heightmap.length-1] = heightmap[0][0];
		heightmap[heightmap.length-1][heightmap.length-1] = heightmap[0][0];

		transformTerrain(new int[] {0, 0}, new int[] {0, heightmap.length}, new int[] {heightmap.length, 0}, new int[] {heightmap.length, heightmap.length}, 0);

		//printHeightmap();

		new DiamondSquare();
	}
}
