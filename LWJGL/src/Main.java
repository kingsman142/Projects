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

public class Main {
	public static final int WIDTH = 1500;
	public static final int HEIGHT = 1000;
	public static final String TITLE = "LWJGL";
	public long window = NULL;
	
	public GLFWKeyCallback keyCallback;
	public GLFWCursorPosCallback cursorCallback;
	public ShaderProgram shaderProgram;
	
	public final int sizeOfFloat = Float.SIZE/Byte.SIZE;
	public final int vertexSize = 4 * sizeOfFloat;
	public final int colorSize = 4 * sizeOfFloat;
	public final int stride = vertexSize + colorSize;
	public final long offsetPosition = 0;
	public final long offsetColor = 4 * sizeOfFloat;
	float[] cameraPosition = new float[3];
	float fovX;
	float fovY;
	float nearZ;
	float farZ;
	float lastX, lastY;
	float offsetX, offsetY;
	DiamondSquare terrain;
	
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
		//glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
		
		shaderProgram = new ShaderProgram();
        shaderProgram.attachVertexShader("VertexShader.glsl");
        shaderProgram.attachFragmentShader("FragmentShader.glsl");
        shaderProgram.link();

        // Generate and bind a Vertex Array
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // The vertices of our Triangle
        float[] vertices = new float[]
        		{	
        			//One cube
        			-0.5f, +0.5f, -0.5f, 1.0f, 1, 0, 0, 1, //Index 0 (top-left)
        			-0.5f, -0.5f, -0.5f, 1.0f, 0, 1, 0, 1, //Index 1 (bottom-left)
        			+0.5f, -0.5f, -0.5f, 1.0f, 0, 0, 1, 1, //Index 2 (bottom-right)
        			+0.5f, +0.5f, -0.5f, 1.0f, 1, 1, 1, 1, //Index 3 (top-right)
        			
        			-0.5f, +0.5f, +0.5f, 1.0f, 0.4f, 0.7f, 0.1f, 1, //4 top-left back
        			-0.5f, -0.5f, +0.5f, 1.0f, 1.0f, 0.5f, 0.0f, 1, //5 bottom-left back
        			
        			+0.5f, +0.5f, +0.5f, 1.0f, 0.0f, 0.9f, 0.5f, 1, //6 top-right back
        			+0.5f, -0.5f, +0.5f, 1.0f, 1.0f, 0.5f, 0.0f, 1 //7 bottom-right back
        		};
        
        //Indices that form a rectangle
        /*short[] indices = new short[]
        		{
        			//One cube
        			//Rectangle points
        			0, 1, 2, //Triangle 1
        			0, 3, 2, //Triangle 2
        			
        			//left
        			0, 1, 4,
        			1, 5, 4,
        			
        			//right
        			3, 2, 7,
        			3, 6, 7,
        			
        			//back
        			4, 5, 7,
        			4, 6, 7,
        			
        			//top
        			0, 3, 4,
        			3, 6, 4,
        			
        			//bottom
        			1, 2, 5,
        			2, 7, 5
        		};*/
        short[] indices = createIndices();
        //VERTICES
        // Create a FloatBuffer of vertices
        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);
        verticesBuffer.put(vertices).flip();

        // Create a Buffer Object and upload the vertices buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 4, GL_FLOAT, false, stride, offsetPosition);
        
        ////////////////////////////////////////
        //INDICES
        // Create a ShortBuffer of indices
        ShortBuffer indicesBuffer = BufferUtils.createShortBuffer(indices.length);
        indicesBuffer.put(indices).flip();

        // Create the Element Buffer object
        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
        ////////////////////////////////////////
        
//        glVertexAttribPointer(0, 4, GL_FLOAT, false, stride, offsetPosition);
        glVertexAttribPointer(1, 4, GL_FLOAT, false, stride, offsetColor);
        
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glBindVertexArray(0);
        
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
        // Clear the screen
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        // Use our program
        shaderProgram.bind();

        // Bind the vertex array
        glBindVertexArray(vaoID);
        
        //Rotates the vertices in real-time
        int timeLocation = glGetUniformLocation(shaderProgram.getID(), "time");
        glUniform1f(timeLocation, time);
        
        //Set the camera position
        if(glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS) cameraPosition[2] += .1f;
        else if(glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS) cameraPosition[2] -= .1f;
        if(glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS) cameraPosition[0] -= .1f;
        else if(glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS) cameraPosition[0] += .1f;
        if(glfwGetKey(window, GLFW_KEY_UP) == GLFW_PRESS) cameraPosition[1] += .1f;
        else if(glfwGetKey(window, GLFW_KEY_DOWN) == GLFW_PRESS) cameraPosition[1] -= .1f;
        int cameraLocation = glGetUniformLocation(shaderProgram.getID(), "cameraPosition");
        glUniform3f(cameraLocation, cameraPosition[0], cameraPosition[1], cameraPosition[2]);
        
        System.out.printf("x: %f, y: %f, z: %f\n", cameraPosition[0], cameraPosition[1], cameraPosition[2]);
        
        //Set the field of view
        fovX = 1.0f;
        fovY = 1.0f;
        int fovxLocation = glGetUniformLocation(shaderProgram.getID(), "fovX");
        glUniform1f(fovxLocation, fovX);
        int fovyLocation = glGetUniformLocation(shaderProgram.getID(), "fovY");
        glUniform1f(fovyLocation, fovY);
        
        nearZ = -1.0f;
        farZ = 1.1f;
        int nearLocation = glGetUniformLocation(shaderProgram.getID(), "nearZ");
        glUniform1f(nearLocation, nearZ);
        int farLocation = glGetUniformLocation(shaderProgram.getID(), "farZ");
        glUniform1f(farLocation, farZ);

        // Draw a triangle of 3 vertices
        //glDrawArrays(GL_TRIANGLES, 0, 36); //Uses only the verticces array
        glDrawElements(GL_TRIANGLES, 36, GL_UNSIGNED_SHORT, 0); //Uses indexing

        glBindVertexArray(0);

        // Un-bind our program
        ShaderProgram.unbind();
    }
	
	public void dispose()
    {
        // Dispose the program
		shaderProgram.dispose();

        // Dispose the vertex array
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
	//Vertex Shader   -> COmpile ->
	
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
			//System.out.println("FPS: " + (float) (frames/now)); 
		}
		
		//Disposes of the window and terminate GLFW
		glfwDestroyWindow(window);
		glfwTerminate();
	}
	
	public float[] createTerrain(){
		terrain = new DiamondSquare();
		terrain.main(null);
		float[] vertices = new float[terrain.heightmap.length*terrain.heightmap.length*8];
		float DFO = (float) terrain.heightmap.length / 2.0f; //Distance From Origin
		int c = 0; //counter
		for(int i = 0; i < terrain.heightmap.length; i++){
			for(int j = 0; j < terrain.heightmap.length; j++){
				vertices[c] = (float) i;
				vertices[c+1] = (float) terrain.heightmap[i][j] * 5;
				vertices[c+2] = (float) j;
				System.out.printf("c: %d, x: %f, y: %f, z: %f\n", c, vertices[c], vertices[c+1], vertices[c+2]);
				vertices[c+3] = 1.0f;
				vertices[c+4] = (float) terrain.heightmap[i][j];
				vertices[c+5] = (float) terrain.heightmap[i][j];
				vertices[c+6] = (float) terrain.heightmap[i][j];
				vertices[c+7] = 1.0f;
				c += 8;
			}
		}
		
		return vertices;
	}
	
	public short[] createIndices(){
		float[] vertices = createTerrain();
		short[] indices = new short[terrain.MAX_SQUARES*3];
		for(int i = 0; i < indices.length; i+=6){
			//First triangle indices
			indices[i] = (short) i;
			indices[i+1] = (short) (i+1);
			indices[i+2] = (short) (i+terrain.heightmap.length);
			//Second triangle indices
			indices[i+3] = (short) (i+1);
			indices[i+4] = (short) (i+terrain.heightmap.length);
			indices[i+5] = (short) (i+terrain.heightmap.length+1);
		}
		
		return indices;
	}

	public static void main(String[] args){
		new Main().start();
	}
}
