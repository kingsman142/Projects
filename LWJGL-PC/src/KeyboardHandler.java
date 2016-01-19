/*
 * James Hahn
 * 
 * This program manages Keyboard Input.
 */

import org.lwjgl.glfw.*;

import static org.lwjgl.glfw.GLFW.*;

//Entirely new class had to be created because GLFWKeyCallback is abstract so it can't be instantiated itself
public class KeyboardHandler extends GLFWKeyCallback{
	//Callback function
	@Override
	public void invoke(long window, int key, int scancode, int action, int mods){
		if(key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE){
			System.out.println("Escape key released!");
			System.exit(0);
		}
		
	}
}
