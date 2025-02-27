/*
 * James Hahn
 * 
 * This program gathers the shader files (currently FragmentShader.glsl and VertexShader.glsl) and compiles them together so they can be used in the project.
 */

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {
	// ProgramID
    int programID;

    // Vertex Shader ID
    int vertexShaderID;
    // Fragment Shader ID
    int fragmentShaderID;

    //Create a new ShaderProgram.
    public ShaderProgram(){
        programID = glCreateProgram();
    }


    //Attach a Vertex Shader to this program.
    public void attachVertexShader(String name){
        //Load the source
        String vertexShaderSource = FileUtil.readFromFile(name);

        //Create the shader and set the source
        vertexShaderID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShaderID, vertexShaderSource);

        //Compile the shader
        glCompileShader(vertexShaderID);

        //Check for errors
        if (glGetShaderi(vertexShaderID, GL_COMPILE_STATUS) == GL_FALSE)
            throw new RuntimeException("Error creating vertex shader\n"
                                       + glGetShaderInfoLog(vertexShaderID, glGetShaderi(vertexShaderID, GL_INFO_LOG_LENGTH)));

        //Attach the shader
        glAttachShader(programID, vertexShaderID);
    }


    //Attach a Fragment Shader to this program.
    public void attachFragmentShader(String name){
        //Read the source
        String fragmentShaderSource = FileUtil.readFromFile(name);

        //Create the shader and set the source
        fragmentShaderID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShaderID, fragmentShaderSource);

        //Compile the shader
        glCompileShader(fragmentShaderID);

        //Check for errors
        if (glGetShaderi(fragmentShaderID, GL_COMPILE_STATUS) == GL_FALSE)
            throw new RuntimeException("Error creating fragment shader\n"
                                       + glGetShaderInfoLog(fragmentShaderID, glGetShaderi(fragmentShaderID, GL_INFO_LOG_LENGTH)));

        //Attach the shader
        glAttachShader(programID, fragmentShaderID);
    }

    //Links this program in order to use.
    public void link(){
        //Link this program
        glLinkProgram(programID);

        //Check for linking errors
        if (glGetProgrami(programID, GL_LINK_STATUS) == GL_FALSE)
            throw new RuntimeException("Unable to link shader program:");
    }

    //Bind this program to use.
    public void bind(){
        glUseProgram(programID);
    }

    //Unbind the shader program.
    public static void unbind(){
        glUseProgram(0);
    }

    //Dispose the program and shaders.
    public void dispose(){
        //Unbind the program
        unbind();

        //Detach the shaders
        glDetachShader(programID, vertexShaderID);
        glDetachShader(programID, fragmentShaderID);

        //Delete the shaders
        glDeleteShader(vertexShaderID);
        glDeleteShader(fragmentShaderID);

        //Delete the program
        glDeleteProgram(programID);
    }

    //@return The ID of this program.
    public int getID(){
        return programID;
    }
}
