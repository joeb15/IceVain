package engine.render.shaders;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL40.glUniform1d;

public class Uniform {

    private int location;

    /**
     * Creates a interface with a uniform given its location
     *
     * @param location The OpenGL location of the uniform
     */
    public Uniform(int location) {
        this.location = location;
    }

    /**
     * Loads a matrix to the shader
     *
     * @param matrix4f The matrix to load
     */
    public void loadMatrix(Matrix4f matrix4f){
        FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(16);
        matrix4f.get(floatBuffer);
        glUniformMatrix4fv(location, false, floatBuffer);
    }

    /**
     * Loads a vector to the shader
     *
     * @param vector3f The vector to load to the shader
     */
    public void loadVector(Vector3f vector3f){
        glUniform3f(location, vector3f.x, vector3f.y, vector3f.z);
    }

    /**
     * Loads a float to the shader
     *
     * @param val The float to load to the shader
     */
    public void loadFloat(float val) {
        glUniform1f(location, val);
    }

    /**
     * Loads a double to the shader
     *
     * @param val The double to load to the shader
     */
    public void loadDouble(double val) {
        glUniform1d(location, val);
    }

    /**
     * Loads an integer to the shader
     *
     * @param val The int to load to the shader
     */
    public void load(int val) {
        glUniform1i(location, val);
    }
}
