import java.util.*;

public class DiamondSquare{
	public static final int LENGTH = 4;
	public static final int MAX_SQUARES = (int) Math.pow(2, LENGTH*2);
	public static Random randomNum = new Random();
	public static double[][] heightmap = new double[(int) Math.pow(2, LENGTH)+1][(int) Math.pow(2, LENGTH)+1];

	public static void printHeightmap(){
		for(int i = 0; i < heightmap.length; i++){
			for(int j = 0; j < heightmap.length; j++){
				System.out.printf("%.4f ", heightmap[i][j]);
			}

			System.out.println();
		}
	}

	public static boolean fullHeightmap(){
		for(int i = 0; i < heightmap.length; i++){
			for(int j = 0; j < heightmap.length; j++){
				if(heightmap[i][j] == 0.0) return false;
			}
		}
		return true;
	}

	public static int plusMinus(){
		double a = randomNum.nextDouble();
		if(a > .5) return 1;
		else return -1;
	}

	public static double averageValue(double value1, double value2, double value3, double value4){
		if(value1 == 0) return (value2+value3+value4)/3;
		else return (value1+value2+value3+value4)/4;
	}

	public static int[] findMidpoint(int width, int height, int[] topLeft){
		return new int[] {topLeft[0] + width/2, topLeft[1] + height/2};
	}

	public static void displaceMidpoint(int[] center, int width, int height, int numIteration){
		double topLeftValue = 0;
		double topRightValue = 0;
		double bottomLeftValue = 0;
		double bottomRightValue = 0;

		//System.out.println("Center: x: " + center[0] + " , y: " + center[1]);
		if(center[1] != 0 && center[0] != 0) topLeftValue = heightmap[center[1] - height/2][center[0] - width/2];
		if(center[1] != 0 && center[0] != heightmap.length) topRightValue = heightmap[center[1] + height/2][center[0] - width/2];
		//System.out.println("BottomRight: " + (center[0] + width/2) + " and " + (center[1] - height/2) + " , height: " + height + " , width: " + width);
		if(center[1] != heightmap.length && center[0] != 0) bottomLeftValue = heightmap[center[1] - height/2][center[0] + width/2];
		//System.out.println("BottomRight: " + (center[0] + width/2) + " and " + (center[1] + height/2) + " , height: " + height + " , width: " + width);
		if(center[1] != heightmap.length && center[0] != heightmap.length) bottomRightValue = heightmap[center[1] + height/2][center[0] + width/2];

		if(heightmap[center[1]][center[0]] == 0) heightmap[center[1]][center[0]] = averageValue(topLeftValue, topRightValue, bottomLeftValue, bottomRightValue) + plusMinus()*(randomNum.nextDouble()/Math.pow(2, numIteration));
		if(heightmap[center[1]][center[0]] < 0) heightmap[center[1]][center[0]] = 0;
		else if(heightmap[center[1]][center[0]] > 1) heightmap[center[1]][center[0]] = 1;
	}

	public static void squareStep(int[] center, int width, int height, int numIteration){
		int left = center[0] - width/2;
		int top = center[1] - height/2;
		int right = center[0] + width/2;
		int bottom = center[1] + height/2;

		if(heightmap[1][left] == 0){
			if(left == 0) heightmap[center[1]][left] = averageValue(0, heightmap[center[1] + height/2][left], heightmap[center[1] - height/2][left], heightmap[center[1]][center[0]]) + plusMinus()*randomNum.nextDouble()/Math.pow(2, numIteration);
			else heightmap[center[1]][left] = averageValue(heightmap[center[1]][left - width/2], heightmap[center[1] + height/2][left], heightmap[center[1] - height/2][left], heightmap[center[1]][center[0]]) + plusMinus()*randomNum.nextDouble()/Math.pow(2, numIteration);

			if(heightmap[center[1]][left] < 0) heightmap[center[1]][left] = 0;
			else if(heightmap[center[1]][left] > 1) heightmap[center[1]][left] = 1;
		}

		if(heightmap[top][center[0]] == 0){
			if(top == 0) heightmap[top][center[0]] = averageValue(0, heightmap[center[1]][center[0]], heightmap[top][center[0] - width/2], heightmap[top][center[0] + width/2]) + plusMinus()*randomNum.nextDouble()/Math.pow(2, numIteration);
			else heightmap[top][center[0]] = averageValue(heightmap[top - height/2][center[0]], heightmap[center[1]][center[0]], heightmap[top][center[0] - width/2], heightmap[top][center[0] + width/2]) + plusMinus()*randomNum.nextDouble()/Math.pow(2, numIteration);

			if(heightmap[top][center[0]] < 0) heightmap[top][center[0]] = 0;
			else if(heightmap[top][center[0]] > 1) heightmap[top][center[0]] = 1;
		}

		if(heightmap[center[1]][right] == 0){
			if(right == heightmap.length-1) heightmap[center[1]][right] = averageValue(0, heightmap[center[1] + height/2][right], heightmap[center[1] - height/2][right], heightmap[center[1]][center[0]]) + plusMinus()*randomNum.nextDouble()/Math.pow(2, numIteration);
			else heightmap[center[1]][right] = averageValue(heightmap[center[1]][right + width/2], heightmap[center[1] + height/2][right], heightmap[center[1] - height/2][right], heightmap[center[1]][center[0]]) + plusMinus()*randomNum.nextDouble()/Math.pow(2, numIteration);

			if(heightmap[center[1]][right] < 0) heightmap[center[1]][right] = 0;
			else if(heightmap[center[1]][right] > 1) heightmap[center[1]][right] = 1;
		}

		if(heightmap[bottom][center[0]] == 0){
			if(bottom == heightmap.length-1) heightmap[bottom][center[0]] = averageValue(0, heightmap[center[1]][center[0]], heightmap[bottom][center[0] - width/2], heightmap[bottom][center[0] + width/2]) + plusMinus()*randomNum.nextDouble()/Math.pow(2, numIteration);
			else heightmap[bottom][center[0]] = averageValue(heightmap[bottom - height/2][center[0]], heightmap[center[1]][center[0]], heightmap[bottom][center[0] - width/2], heightmap[bottom][center[0] + width/2]) + plusMinus()*randomNum.nextDouble()/Math.pow(2, numIteration);

			if(heightmap[bottom][center[0]] < 0) heightmap[bottom][center[0]] = 0;
			else if(heightmap[bottom][center[0]] > 1) heightmap[bottom][center[0]] = 1;
		}
	}

	public static void transformTerrain(int[] topLeft, int[] topRight, int[] bottomLeft, int[] bottomRight, int numIteration){
		int numSquares = (int) Math.pow(2, numIteration+2)/4;
		if(!fullHeightmap() && numSquares <= MAX_SQUARES){
			//int numSquares = (int) Math.pow(2, numIteration+2)/4;
			int squaresLength = (int) Math.pow(numSquares, .5);
			int lengthRatio = (heightmap.length-1)/squaresLength;
			int lengthRatio2 = (heightmap.length-1)/squaresLength / 2;
			System.out.println("NumSquares: " + numSquares + " , SquaresLength: " + squaresLength);
			//System.out.println("Heightmap.length-1/squareslength: " + lengthRatio + " and " + lengthRatio2);
			int width = topRight[1] - topLeft[1];
			int height = bottomLeft[0] - topLeft[0];

			for(int i = 0; i < squaresLength; i++){
				for(int j = 0; j < squaresLength; j++){
					int[] center = findMidpoint(lengthRatio, lengthRatio, new int[] {topLeft[0] /*+ lengthRatio2*i*/ + lengthRatio*i, topLeft[1] /*+ lengthRatio2*j*/ + lengthRatio*j});
					displaceMidpoint(center, lengthRatio, lengthRatio, numIteration+1);
					//System.out.printf("Center: x: %d, y: %d, value: %f\n", center[0], center[1], heightmap[center[0]][center[1]]);
					squareStep(center, lengthRatio, lengthRatio, numIteration+1);
					//printHeightmap();
				}
			}

			for(int i = 0; i < squaresLength; i++){
				for(int j = 0; j < squaresLength; j++){
					transformTerrain(topLeft, topRight, bottomLeft, bottomRight, numIteration+2);
				}
			}
		} else{
			return;
		}
	}

	public static void main(String[] args){
		heightmap[0][0] = randomNum.nextDouble();
		heightmap[heightmap.length-1][0] = heightmap[0][0];
		heightmap[0][heightmap.length-1] = heightmap[0][0];
		heightmap[heightmap.length-1][heightmap.length-1] = heightmap[0][0];

		transformTerrain(new int[] {0, 0}, new int[] {0, heightmap.length}, new int[] {heightmap.length, 0}, new int[] {heightmap.length, heightmap.length}, 0);

		printHeightmap();
	}
}
