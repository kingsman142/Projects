/*
James Hahn

This program outputs a two-dimensional random terrain to the user through a JFrame.  It utilizes the midpoint displacement algorithm
as this was the first and one of the most popular algorithms for creating 2D terrain that I could find.

For the user, I strongly encourage the user to experiment changing two variables: SMOOTHNESS and LENGTH, both global public constants.
*/

import java.util.Random;
import java.awt.*;
import javax.swing.*;

public class RandomTerrain extends JFrame{
	//Set the constants for the terrain
	//WIDTH and HEIGHT are the size of the window
	//SMOOTHNESS is a constant to determine how smooth the terrain is (higher smoothness = smoother terrain)
	//LENGTH is the power of two (2^LENGTH) length of the one-dimensional terrain array
	public static final int HEIGHT = 750;
	public static final int WIDTH = 500;
	public static final int SMOOTHNESS = 3;
	public static final int LENGTH = 10;

	public static Random randomNum = new Random();
	public static double[] linearTerrain;

	public RandomTerrain(){
		//Set the properties of the JFrame
		setTitle("Terrain");
		setSize(linearTerrain.length, HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackground(new Color(137, 207, 240)); //this gives the background a nice baby blue sky look
		setVisible(true);
	}

	//Find the average value between two points
	public static double averageValue(double leftValue, double rightValue){
		return (leftValue + rightValue)/2.0;
	}

	//Check to see if the terrain's values are completely set (no value is 0)
	public static boolean fullTerrain(double[] terrain){
		for(double a: terrain){
			if(a == 0.0) return false;
		}
		return true;
	}

	//Paint the terrain to the screen
	public void paint(Graphics g){
		for(int i = 0; i < linearTerrain.length-1; i++){
			//Without the "WIDTH - ", the terrain would be upside-down
			g.drawLine(i, WIDTH - (int) linearTerrain[i], (i+1), WIDTH - (int) linearTerrain[i+1]);

			//This draws polygons so the area under the terrain (the ground) looks nicer
			int[] xPoints = {i, i, i+1, i+1};
			int[] yPoints = {HEIGHT, WIDTH - (int) linearTerrain[i], WIDTH - (int) linearTerrain[i+1], HEIGHT};
			g.drawPolygon(xPoints, yPoints, 4);
		}
	}

	//Randomly determines if the displacement will be positive or negative
	public static int plusMinus(){
		double a = randomNum.nextDouble();
		if(a > .5) return 1;
		else return -1;
	}

	//Displace the midpoint
	public static void displaceMidpoint(double[] terrain, int index, int lowerIndex, int upperIndex, int numIteration){
		terrain[index] = averageValue(terrain[lowerIndex], terrain[upperIndex]) + plusMinus()*(randomNum.nextDouble()/Math.pow(2, numIteration))*(1000/SMOOTHNESS);
	}

	//Recursive method that depending on the current iteration, will determine how many midpoints there are,
	//the length of each segment, and then will displace all of the midpoints of the segments, and finally
	//calls itself
	public static void transformTerrain(double[] terrain, int left, int right, int numIteration){
		if(!fullTerrain(terrain)){
			int numMidpoints = (int) Math.pow(2, numIteration);
			int segmentLength = (int) ((terrain.length-1)/numMidpoints);
			for(int i = 1; i < numMidpoints; i+=2){
				displaceMidpoint(terrain, i*segmentLength, (i-1)*segmentLength, (i+1)*segmentLength, numIteration);
			}

			transformTerrain(terrain,  0, terrain.length-1, numIteration+1);
		} else{
			return;
		}
	}

	//Optionally print the terrain values to the console
	public static void printTerrain(double[] terrain){
		for(int i = 0; i < terrain.length; i ++){
			System.out.println(terrain[i]);
		}
	}

	public static void main(String[] args){
		linearTerrain = new double[(int) Math.pow(2, LENGTH) + 1];

		//Set the initial left and right values of the sides of the terrain
		//Make the right side of the terrain the same height as the left side so the terrain wraps
		linearTerrain[0] = randomNum.nextDouble()/2*500;
		linearTerrain[linearTerrain.length-1] = linearTerrain[0];

		transformTerrain(linearTerrain,  0, linearTerrain.length-1, 1);

		//Prints out the terrain to the console
		//printTerrain(linearTerrain);

		//Instantiates the window and everything so the user can see the terrain
		RandomTerrain randomTerrain = new RandomTerrain();
	}
}
