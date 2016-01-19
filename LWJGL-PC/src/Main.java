/*
 * James Hahn
 * 
 * This program utilizes LWJGL and my past DiamondSquare.java file to create a 3D randomly-generated terrain.
 */

import org.lwjgl.BufferUtils;
import java.nio.*;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Main{
	//Window properties
	public static final int WIDTH = 1024;
	public static final int HEIGHT = 768;
	public static final String TITLE = "LWJGL";
	public long window = NULL;
	
	public final float SMOOTHNESS = 0.2f; //Smoothness constant for the terrain
	
	//Callbacks for OpenGL to manage input
	public GLFWKeyCallback keyCallback;
	public GLFWCursorPosCallback cursorCallback;
	public ShaderProgram shaderProgram;
	
	//Constants to determine the size in space of each vertex
	public final int sizeOfFloat = Float.SIZE/Byte.SIZE;
	public final int vertexSize = 4 * sizeOfFloat;
	public final int colorSize = 4 * sizeOfFloat;
	public final int stride = vertexSize + colorSize;
	public final long offsetPosition = 0;
	public final long offsetColor = 4 * sizeOfFloat;
	
	//Variables for properties of the camera
	float[] cameraPosition = new float[3];
	float fovX;
	float fovY;
	float nearZ;
	float farZ;
	float lastX, lastY;
	float offsetX, offsetY;
	float cameraSpeed = 0.4f;
	
	//Terrain reference
	DiamondSquare terrain;
	
	//ID values for each Vertex Array and Index Array (keep track of which one is which)
	public int vaoID;
	public int vboID;
	public int eboID;
	
	//Initializes everything and creates the window
	public void init(){
		//Initialize GLFW and display an error message if it fails
		if(glfwInit() == GL_FALSE){
			System.out.println("Failed to start up GLFW!");
			System.exit(0);
		}
		
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
		
		//width, height, title, monitor (forces window to be windowed), share (null so it isn't shared to other monitors)
		window = glfwCreateWindow(WIDTH, HEIGHT, TITLE, NULL, NULL);
		
		if(window == NULL){
			System.out.println("Failed to create window!");
			System.exit(1);
		}
		
		//Make the window the current context (it's being focused on)
		glfwMakeContextCurrent(window);
		//Creates the LWJGL context from the above GLFW context
		GL.createCapabilities();
		//The above 2 contexts should refresh immediately when the buffers are swapped
		glfwSwapInterval(1);
		
		//Manages all of the keyboard input and mouse position
		glfwSetKeyCallback(window, keyCallback = new KeyboardHandler());
		glfwSetCursorPosCallback(window, cursorCallback = new MouseHandler());
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LESS);
		//glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED); //Can be used to disable the cursor
		
		shaderProgram = new ShaderProgram();
        shaderProgram.attachVertexShader("VertexShader.glsl");
        shaderProgram.attachFragmentShader("FragmentShader.glsl");
        shaderProgram.link();

        // Generate and bind a Vertex Array
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        //The vertices of the terrain
        float[] vertices = createTerrain();
        
        //Indices that form each triangle in the terrain
        int[] indices = createIndices();
        
        //VERTICES
        //Create a FloatBuffer of vertices
        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);
        verticesBuffer.put(vertices).flip();

        //Create a Buffer Object and upload the vertices buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 4, GL_FLOAT, false, stride, offsetPosition);
        
        //Create an IntBuffer of indices
        IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indices.length);
        indicesBuffer.put(indices).flip();

        //Create the Element Buffer object
        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
        
        //glVertexAttribPointer(0, 4, GL_FLOAT, false, stride, offsetPosition);
        glVertexAttribPointer(1, 4, GL_FLOAT, false, stride, offsetColor);
        
        //Enable the vertex arrays
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glBindVertexArray(0);
        
        //Set the default values for these variables just to make sure
        cameraPosition[0] = 0.0f;
        cameraPosition[1] = 0.0f;
        cameraPosition[2] = 0.0f;
        fovX = 0.0f;
        fovY = 0.0f;
        nearZ = 0.0f;
        farZ = 0.0f;
        lastX = WIDTH/2;
        lastY = HEIGHT/2;
	}
	
	public void render(float time)
    {
        //Clear the screen
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        //Bind the shaders to the program
        shaderProgram.bind();

        //Bind the vertex array, vaoID, to the program
        glBindVertexArray(vaoID);
        
        //Updates the time variable in the shader files
        int timeLocation = glGetUniformLocation(shaderProgram.getID(), "time");
        glUniform1f(timeLocation, time);
        
        //Set the camera properties
        if(glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS) cameraPosition[2] += cameraSpeed; //Move forward
        else if(glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS) cameraPosition[2] -= cameraSpeed; //Move backward
        if(glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS) cameraPosition[0] -= cameraSpeed; //Move left
        else if(glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS) cameraPosition[0] += cameraSpeed; //Move right
        if(glfwGetKey(window, GLFW_KEY_UP) == GLFW_PRESS) cameraPosition[1] += cameraSpeed; //Move up
        else if(glfwGetKey(window, GLFW_KEY_DOWN) == GLFW_PRESS) cameraPosition[1] -= cameraSpeed; //Move down
        
        int cameraLocation = glGetUniformLocation(shaderProgram.getID(), "cameraPosition"); //Find the cameraPosition variable in the shader file
        glUniform3f(cameraLocation, cameraPosition[0], cameraPosition[1], cameraPosition[2]); //Set the camera position
        
        //Set the field of view
        fovX = 1.0f;
        fovY = 1.0f;
        int fovxLocation = glGetUniformLocation(shaderProgram.getID(), "fovX");
        glUniform1f(fovxLocation, fovX);
        int fovyLocation = glGetUniformLocation(shaderProgram.getID(), "fovY");
        glUniform1f(fovyLocation, fovY);
        
        //Set near and far Z for the projection matrix
        nearZ = -1.0f;
        farZ = 1.1f;
        int nearLocation = glGetUniformLocation(shaderProgram.getID(), "nearZ");
        glUniform1f(nearLocation, nearZ);
        int farLocation = glGetUniformLocation(shaderProgram.getID(), "farZ");
        glUniform1f(farLocation, farZ);

        //Draw all of the triangles
        //glDrawArrays(GL_TRIANGLES, 0, 2105344); //Uses only the vertices array
        glDrawElements(GL_TRIANGLES, 2105344, GL_UNSIGNED_INT, 0); //Uses indexing (MAX_SQUARES = 262,144 (*3 = 786432)) try 2105344

        glBindVertexArray(0);

        //Un-bind the shaders from the program
        ShaderProgram.unbind();
    }
	
	public void dispose()
    {
        //Dispose the program
		shaderProgram.dispose();

        //Dispose the vertex array
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoID);

        // Dispose the buffer object
        //glBindBuffer(GL_ARRAY_BUFFER, 0);
        //glDeleteBuffers(vboID);
        
        // Dispose the element buffer object
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glDeleteBuffers(eboID);
    }
	
	//This is the program compile process
	//Fragment Shader -> Compile ->
	//								Program -> Link
	//Vertex Shader   -> Compile ->
	
	//Starts the render-loop
	//Ends with disposing of the window
	public void start(){
		float now, last, delta;
		last = 0;
		
		//Initialize everything
		init();
		glClearColor(.5f, 0.5f, .9f, 1.0f);
		
		int frames = 0;
		
		//This is the render loop
		while(glfwWindowShouldClose(window) == GL_FALSE){
			//Get the current game time (not like a clock time)
			now = (float) glfwGetTime();
			delta = last - now;
			last = now;
			
			//Update and render
			//update(delta);
			render(now);
			
			glfwPollEvents();
			glfwSwapBuffers(window);
			
			frames++;
			//System.out.println("FPS: " + (float) (frames/now));  //FPS Counter
		}
		
		//Disposes of the window and terminate GLFW
		glfwDestroyWindow(window);
		glfwTerminate();
	}
	
	public float[] createTerrain(){
		//Create the terrain object and generate terrain in real-time
		terrain = new DiamondSquare();
		terrain.main(null);
		
		
		float[] vertices = new float[terrain.heightmap.length*terrain.heightmap.length*8]; //Square the heightmap length, and multiply by 8 because each vertex has 8 properties
		int c = 0; //counter to go through the array; independent of the terrain coordinates, i and j
		
		for(int i = 0; i < terrain.heightmap.length; i++){
			for(int j = 1; j < terrain.heightmap.length; j++){
				//Vertex coordinates
				vertices[c] = (float) i * SMOOTHNESS; //X
				vertices[c+1] = (float) terrain.heightmap[i][j] * 10f - 5f; //Y
				vertices[c+2] = (float) j * SMOOTHNESS; //Z
				vertices[c+3] = 1.0f; //W, 0.0f for a velocity and 1.0f for a point in space
				
				//Vertex color values
				float r = 0.0f;
				float g = 0.0f;
				float b = 0.0f;
				if(terrain.heightmap[i][j] < .33f){
					r = (float) (44.0/255.0);
					g = (float) (176.0/255.0);
					b = (float) (55.0/255.0);
				} else if(terrain.heightmap[i][j] >= .33f && terrain.heightmap[i][j] < .66f){
					r = (float) (133.0/255.0);
					g = (float) (159.0/255.0);
					b = (float) (168.0/255.0);
				} else if(terrain.heightmap[i][j] >= .66f){
					r = (float) (255.0/255.0);
					g = (float) (255.0/255.0);
					b = (float) (255.0/255.0);
				}
				vertices[c+4] = (float) terrain.heightmap[i][j]; //Replace with r to get colorful terrain
				vertices[c+5] = (float) terrain.heightmap[i][j]; //Replace with g to get colorful terrain
				vertices[c+6] = (float) terrain.heightmap[i][j]; //Replace with b to get colorful terrain
				vertices[c+7] = 1.0f; //Opacity
				c += 8;
			}
		}
		
		return vertices;
	}
	
	public int[] createIndices(){
		int[] indices = new int[terrain.MAX_SQUARES*6];
		int c = 0; //Counter because each triangle needs to move 1 every time, so it has to be independent from i, which increments by 6
		
		for(int i = 0; i < indices.length-15360; i += 6){
				//First triangle indices
				indices[i] = c;
				indices[i+1] = c+1;
				indices[i+2] = c+terrain.heightmap.length;
				
				//Second triangle indices
				indices[i+3] = c+1;
				indices[i+4] = c+terrain.heightmap.length;
				indices[i+5] = c+terrain.heightmap.length+1;
				
				//At the 506th point in every row, just go to the next row because it'll generate undesired results
				if(c % 512 == 506) c += 6;
				else c++;
		}
		
		return indices;
	}

	public static void main(String[] args){
		new Main().start(); //Start the whole thing up
	}
}
