package engine.render.renderers;

import engine.render.models.RawModel;
import engine.render.shaders.WorldShader;
import engine.utils.Camera;
import engine.world.Chunk;
import engine.world.World;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class WorldRenderer {

    private WorldShader shader;
    private Camera camera;
    private World world;

    /**
     * Renders the world and entities
     *
     * @param world The world to render
     * @param camera The camera to use for the view matrix
     */
    public WorldRenderer(World world, Camera camera){
        this.camera = camera;
        shader = new WorldShader();
        shader.bind();
        shader.loadProjectionMatrix(camera);
        shader.loadTexture();
        shader.unbind();
        this.world=world;
    }

    /**
     * Renders the world and entities
     */
    public void render(){
        prepare();
        renderWorld();
        shader.unbind();
    }

    /**
     * Prepares the shader and entity hash for rendering
     */
    public void prepare(){
        shader.bind();
        shader.loadViewMatrix(camera);
        shader.loadAmbient(new Vector3f(.1f,.1f,.1f));
        shader.loadLights(world.getLights());
    }

    /**
     * Renders the world
     */
    public void renderWorld(){
        shader.loadTransformationMatrix(new Matrix4f());
        shader.loadMaterialLibrary(world.getMaterialLibrary());
        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++) {
                Chunk chunk = world.getChunk(i, j);
                if(chunk!=null) {
                    RawModel rawModel = chunk.getModel();
                    glBindVertexArray(rawModel.getVaoId());

                    for (int c : shader.attribs)
                        glEnableVertexAttribArray(c);

                    glDrawElements(GL_TRIANGLES, rawModel.getVertexCount(), GL_UNSIGNED_INT, 0);

                    for (int c : shader.attribs)
                        glDisableVertexAttribArray(c);

                    glBindVertexArray(0);
                }
            }
        }
    }

    /**
     * Frees all previously allocated memory
     */
    public void cleanUp(){
        shader.cleanUp();
    }

}
