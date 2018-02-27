package engine.render.models;

public class RawModel {

    private int vaoId, vertexCount;

    /**
     * A storage class for all of the data needed to render a model
     *
     * @param vaoId The VAO id associated with the model
     * @param vertexCount The number of vertices to draw
     */
    public RawModel(int vaoId, int vertexCount) {
        this.vaoId = vaoId;
        this.vertexCount = vertexCount;
    }

    /**
     * Getter for the VAO's ID
     *
     * @return The id of the VAO
     */
    public int getVaoId() {
        return vaoId;
    }

    /**
     * Getter for the vertex count
     *
     * @return The number of vertices to draw
     */
    public int getVertexCount() {
        return vertexCount;
    }
}
