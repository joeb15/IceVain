package engine.utils;

import engine.render.models.RawModel;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_INT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

public class Loader {

    private static ArrayList<Integer> vaos = new ArrayList<>();
    private static ArrayList<Integer> vbos = new ArrayList<>();

    /**
     * Loads a model to a VAO given it's positions, normals, textureCoords, and indices
     *
     * @param positions The <code>float[]</code> of positions
     * @param normals The <code>float[]</code> of normals
     * @param tangents The <code>float[]</code> of tangents
     * @param textureCoords The <code>float[]</code> of texture coordinates
     * @param indicies The <code>int[]</code> of indices
     * @param materialsArray The <code>int[]</code> of materials
     * @return The model represented as a <code>RawModel</code>
     */
    public static RawModel loadToVAO(float[] positions, float[] normals, float[] tangents, float[] textureCoords, int[] indicies, int[] materialsArray){
        int vaoID = createVAO();
        bindIndicesBuffer(indicies);
        storeDataInAttributeList(0, positions, 3);
        storeDataInAttributeList(1, textureCoords, 2);
        storeDataInAttributeList(2, normals, 3);
        storeDataInAttributeList(3, materialsArray, 1);
        storeDataInAttributeList(4, tangents, 3);
        unbindVAO();
        return new RawModel(vaoID, indicies.length);
    }

    /**
     * Binds an index buffer
     *
     * @param indices The indices to add to the buffer
     */
    private static void bindIndicesBuffer(int[] indices) {
        int vboID = glGenBuffers();
        vbos.add(vboID);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, createBuffer(indices), GL_STATIC_DRAW);
    }

    /**
     * Creates a VAO to use for storing attributes
     *
     * @return The VAO's ID
     */
    private static int createVAO(){
        int vaoID = glGenVertexArrays();
        vaos.add(vaoID);
        glBindVertexArray(vaoID);
        return vaoID;
    }

    /**
     * Stores data in an attribute array to use in the shaders
     *
     * @param attributeNumber The attribute to store the data in
     * @param data The <code>float[]</code> with all of the data
     * @param size The size of each bit of data i.e. <code>Vector3f</code> has a size of 3
     */
    private static void storeDataInAttributeList(int attributeNumber, float[] data, int size){
        int vboID = glGenBuffers();
        vbos.add(vboID);
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, createBuffer(data), GL_STATIC_DRAW);
        glVertexAttribPointer(attributeNumber, size, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    /**
     * Stores data in an attribute array to use in the shaders
     *
     * @param attributeNumber The attribute to store the data in
     * @param data The <code>int[]</code> with all of the data
     * @param size The size of each bit of data i.e. <code>Vector3i</code> has a size of 3
     */
    private static void storeDataInAttributeList(int attributeNumber, int[] data, int size){
        int vboID = glGenBuffers();
        vbos.add(vboID);
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, createBuffer(data), GL_STATIC_DRAW);
        glVertexAttribPointer(attributeNumber, size, GL_INT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    /**
     * Unbinds the current VAO
     */
    private static void unbindVAO(){
        glBindVertexArray(0);
    }

    /**
     * Creates a buffer from an array of data
     *
     * @param data The array to convert to a buffer
     * @return The flipped buffer of data
     */
    private static IntBuffer createBuffer(int[] data){
        IntBuffer intBuffer = BufferUtils.createIntBuffer(data.length);
        intBuffer.put(data);
        intBuffer.flip();
        return intBuffer;
    }

    /**
     * Creates a buffer from an array of data
     *
     * @param data The array to convert to a buffer
     * @return The flipped buffer of data
     */
    private static FloatBuffer createBuffer(float[] data){
        FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(data.length);
        floatBuffer.put(data);
        floatBuffer.flip();
        return floatBuffer;
    }

    /**
     * Frees up all allocated memory on program close
     */
    public static void cleanUp(){
        for(int i:vaos)
            glDeleteVertexArrays(i);
        for(int i:vbos)
            glDeleteBuffers(i);
    }

}
