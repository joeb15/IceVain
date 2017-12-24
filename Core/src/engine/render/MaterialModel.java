package engine.render;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

public class MaterialModel extends Model{

    private int materialId;

    public MaterialModel(float[] vertices, int[] materials, int[] indices){
        drawCount = indices.length;

        vertexId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexId);
        glBufferData(GL_ARRAY_BUFFER, createBuffer(vertices), GL_STATIC_DRAW);

        materialId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, materialId);
        glBufferData(GL_ARRAY_BUFFER, createBuffer(materials), GL_STATIC_DRAW);

        indexId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, createBuffer(indices), GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    /**
     * Binds the model to the openGL rendering pipeline
     */
    @Override
    public void bind(){
        glEnableClientState(GL_VERTEX_ARRAY);

        glBindBuffer(GL_ARRAY_BUFFER, vertexId);
        glVertexPointer(3, GL_FLOAT, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, materialId);
        glVertexPointer(1, GL_FLOAT, 0, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexId);
    }

    /**
     * Renders an instance of the model
     */
    @Override
    public void render(){
        glDrawElements(GL_TRIANGLES, drawCount, GL_UNSIGNED_INT, 0);
    }

    /**
     * Unbinds all references to this model from the openGL pipeline
     */
    @Override
    public void unbind(){
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDisableClientState(GL_VERTEX_ARRAY);
    }
}
