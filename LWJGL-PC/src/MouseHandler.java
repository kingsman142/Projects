/*
 * James Hahn
 * 
 * This program manages mouse input.
 */

import org.lwjgl.glfw.*;

public class MouseHandler extends GLFWCursorPosCallback{
	@Override
	public void invoke(long window, double xPos, double yPos){
		//System.out.printf("X pos: %.2f, Y pos: %.2f\n", xPos, yPos);
		
	}
}
