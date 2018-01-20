package engine.render.shaders;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL40.glUniform1d;

public class Uniform {

    private int location;

    public Uniform(int location) {
        this.location = location;
    }

    public void loadMatrix(Matrix4f matrix4f){
        FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(16);
        matrix4f.get(floatBuffer);
        glUniformMatrix4fv(location, false, floatBuffer);
    }

    public void loadVector(Vector3f vector3f){
        glUniform3f(location, vector3f.x, vector3f.y, vector3f.z);
    }

    public void loadFloat(float val) {
        glUniform1f(location, val);
    }

    public void loadDouble(double val) {
        glUniform1d(location, val);
    }

    public void load(int val) {
        glUniform1i(location, val);
    }
}
