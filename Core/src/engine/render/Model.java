package engine.render;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

public class Model {

    private int drawCount;
    private int vertexId, textureId, indexId;

    private static float[] vertices = new float[]{
            -.5f,.5f,.5f,.5f,.5f,-.5f,-.5f,-.5f
    };

    private static float[] texture = new float[]{
            0,0,1,0,1,1,0,1
    };
    private static int[] indices = new int[]{
            0,1,2,2,3,0
    };

    public Model(){
        this(vertices, texture, indices);
    }

    public Model(float[] vertices, float[] texture, int[] indices){
        drawCount = indices.length;

        vertexId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexId);
        glBufferData(GL_ARRAY_BUFFER, createBuffer(vertices), GL_STATIC_DRAW);

        textureId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, textureId);
        glBufferData(GL_ARRAY_BUFFER, createBuffer(texture), GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        indexId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, createBuffer(indices), GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    private FloatBuffer createBuffer(float[] floats) {
        FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(floats.length);
        floatBuffer.put(floats);
        floatBuffer.flip();
        return floatBuffer;
    }

    private IntBuffer createBuffer(int[] ints) {
        IntBuffer intBuffer = BufferUtils.createIntBuffer(ints.length);
        intBuffer.put(ints);
        intBuffer.flip();
        return intBuffer;
    }

    public void bind(){
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);

        glBindBuffer(GL_ARRAY_BUFFER, vertexId);
        glVertexPointer(2, GL_FLOAT, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, textureId);
        glTexCoordPointer(2, GL_FLOAT, 0, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexId);
    }

    public void render(){
        glDrawElements(GL_TRIANGLES, drawCount, GL_UNSIGNED_INT, 0);
    }

    public void unbind(){
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDisableClientState(GL_TEXTURE_COORD_ARRAY);
        glDisableClientState(GL_VERTEX_ARRAY);
    }

}
