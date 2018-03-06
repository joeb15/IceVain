package engine.render.models;

import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class VertexNM {

    private static final int NO_INDEX = -1;

    private Vector3f position;
    private int textureIndex = NO_INDEX;
    private int normalIndex = NO_INDEX;
    private VertexNM duplicateVertex = null;
    private int index;
    private int material;
    private float length;
    private List<Vector3f> tangents = new ArrayList<>();
    private Vector3f averagedTangent = new Vector3f(0, 0, 0);

    /**
     * Storage class for information regarding vertices
     *
     * @param index The index of the vertex
     * @param position The position of the vertex
     */
    protected VertexNM(int index, Vector3f position){
        this.index = index;
        this.position = position;
        this.length = position.length();
    }

    /**
     * Adds a calculated tangent to the face
     *
     * @param tangent The tangent face
     */
    protected void addTangent(Vector3f tangent){
        tangents.add(tangent);
    }

    /**
     * Duplicates a vertex
     *
     * @param newIndex The new index of the vertex
     * @return The duplicate vertex
     */
    protected VertexNM duplicate(int newIndex){
        VertexNM vertex = new VertexNM(newIndex, position);
        vertex.tangents = this.tangents;
        return vertex;
    }

    /**
     * Averages the tangents for the vertex
     */
    protected void averageTangents(){
        if(tangents.isEmpty()){
            return;
        }
        for(Vector3f tangent : tangents){
            averagedTangent.add(tangent);
        }
        averagedTangent.normalize();
    }

    /**
     * Returns the average of the tangents
     *
     * @return the averaged tangent
     */
    protected Vector3f getAverageTangent(){
        return averagedTangent;
    }

    /**
     * Gets the index of the vertex
     *
     * @return The index of the vertex
     */
    protected int getIndex(){
        return index;
    }

    /**
     * Gets the distance of the vertex from the origin
     *
     * @return The length of the vertex
     */
    protected float getLength(){
        return length;
    }

    /**
     * Returns whether the vertex has a texture and normal index
     *
     * @return Whether the vertex is unset or not
     */
    protected boolean isUnset(){
        return textureIndex == NO_INDEX || normalIndex == NO_INDEX;
    }

    /**
     * Returns whether the vertex has the same texture and normal indices
     *
     * @param textureIndexOther The texture index of the other vertex
     * @param normalIndexOther The normal index of the other vertex
     * @return Whether the indices are the same
     */
    protected boolean hasSameTextureAndNormal(int textureIndexOther,int normalIndexOther){
        return textureIndexOther==textureIndex && normalIndexOther==normalIndex;
    }

    /**
     * Setter for the texture index
     *
     * @param textureIndex The new texture index to use
     */
    protected void setTextureIndex(int textureIndex){
        this.textureIndex = textureIndex;
    }

    /**
     * Setter for the normal index
     *
     * @param normalIndex The new normal index to use
     */
    protected void setNormalIndex(int normalIndex){
        this.normalIndex = normalIndex;
    }

    /**
     * Gets the position of the current vertex
     *
     * @return The position of the vertex
     */
    protected Vector3f getPosition() {
        return position;
    }

    /**
     * Getter for the texture index of the vertex
     *
     * @return The texture index of the vertex
     */
    protected int getTextureIndex() {
        return textureIndex;
    }

    /**
     * Getter for the normal index of the model
     *
     * @return The normal index of the model
     */
    protected int getNormalIndex() {
        return normalIndex;
    }

    /**
     * Getter for the duplicate vertex
     *
     * @return The duplicate vertex
     */
    protected VertexNM getDuplicateVertex() {
        return duplicateVertex;
    }

    /**
     * Setter for the duplicate vertex
     *
     * @param duplicateVertex The new duplicate vertex to use
     */
    protected void setDuplicateVertex(VertexNM duplicateVertex) {
        this.duplicateVertex = duplicateVertex;
    }

    /**
     * Setter for the material of the vertex
     *
     * @param material The material of the vertex
     */
    public void setMaterial(int material) {
        this.material = material;
    }

    /**
     * Getter for the material of the vertex
     *
     * @return The material of the vertex
     */
    public int getMaterial() {
        return material;
    }
}