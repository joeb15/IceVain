package engine.render.shaders;

import engine.utils.Debug;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengles.GLES20.GL_TRUE;

public abstract class Shader {

    private int program;
    private int vert, frag;

    public ArrayList<Integer> attribs = new ArrayList<>();

    /**
     * Generic shader class to load shader files and handle uniforms
     *
     * @param vertexShader The file of the vertex shader
     * @param fragmentShader The file of the fragment shader
     */
    public Shader(File vertexShader, File fragmentShader){
        program = glCreateProgram();

        vert = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vert, readFile(vertexShader));
        glCompileShader(vert);
        if(glGetShaderi(vert, GL_COMPILE_STATUS)!=GL_TRUE){
            Debug.error(glGetShaderInfoLog(vert));
            System.exit(1);
        }

        frag = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(frag, readFile(fragmentShader));
        glCompileShader(frag);
        if(glGetShaderi(frag, GL_COMPILE_STATUS)!=GL_TRUE){
            Debug.error(glGetShaderInfoLog(frag));
            System.exit(1);
        }

        glAttachShader(program, vert);
        glAttachShader(program, frag);

        bindAttributes();

        glLinkProgram(program);
        if(glGetProgrami(program, GL_LINK_STATUS)!=1){
            Debug.error(glGetProgramInfoLog(program));
            System.exit(1);
        }

        glValidateProgram(program);
        if(glGetProgrami(program, GL_VALIDATE_STATUS)!=1){
            Debug.error(glGetProgramInfoLog(program));
            System.exit(1);
        }

        getUniformLocations();
    }


    /**
     * Gets all uniform locations
     */
    public abstract void getUniformLocations();

    /**
     * Binds all attributes
     */
    public abstract void bindAttributes();

    /**
     * Binds an attribute to an index
     *
     * @param index The index of the attribute
     * @param name The name of the attribute
     */
    public void bindAttribute(int index, String name){
        attribs.add(index);
        glBindAttribLocation(program, index, name);
    }

    /**
     * Binds the shader
     */
    public void bind(){
        glUseProgram(program);
    }

    /**
     * Unbinds the current shader
     */
    public void unbind(){
        glUseProgram(0);
    }

    /**
     * Frees all allocated memory upon the end of a program
     */
    public void cleanUp(){
        unbind();
        glDetachShader(program, vert);
        glDetachShader(program, frag);
        glDeleteShader(vert);
        glDeleteShader(frag);
        glDeleteProgram(program);
    }

    /**
     * Helper method to get the string contents of a file
     *
     * @param file The file to be read
     * @return The string containing all of the text within the file
     */
    private String readFile(File file){
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            while((line=bufferedReader.readLine())!=null){
                stringBuilder.append(line);
                stringBuilder.append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /**
     * Gets a uniform location from the shader
     *
     * @param name The name of the uniform
     * @return A <code>Uniform</code> that can be used to load variables to
     */
    public Uniform getUniform(String name) {
        int location = glGetUniformLocation(program, name);
        return new Uniform(location);
    }

    /**
     * Gets a struct of uniform variables
     *
     * @param name The name of the uniform struct
     * @param properties The variables within the uniform struct
     * @return The <code>UniformStruct</code> that can be used to represent the struct
     */
    public UniformStruct getUniform(String name, String ... properties){
        int[] locations = new int[properties.length];
        for(int i=0;i<locations.length;i++){
            locations[i]=glGetUniformLocation(program,name+"."+properties[i]);
        }
        return new UniformStruct(locations);
    }
}
