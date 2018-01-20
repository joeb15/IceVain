package engine.render.shaders;

import engine.utils.Debug;

import java.io.*;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengles.GLES20.GL_TRUE;

public abstract class Shader {

    private int program;
    private int vert, frag;

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

    public abstract void getUniformLocations();

    public abstract void bindAttributes();

    public void bindAttribute(int index, String name){
        glBindAttribLocation(program, index, name);
    }

    public void bind(){
        glUseProgram(program);
    }

    public void unbind(){
        glUseProgram(0);
    }

    public void cleanUp(){
        unbind();
        glDetachShader(program, vert);
        glDetachShader(program, frag);
        glDeleteShader(vert);
        glDeleteShader(frag);
        glDeleteProgram(program);
    }

    private String readFile(File file){
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            while((line=bufferedReader.readLine())!=null){
                stringBuilder.append(line);
                stringBuilder.append('\n');
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public Uniform getUniform(String name) {
        int location = glGetUniformLocation(program, name);
        return new Uniform(location);
    }
}
