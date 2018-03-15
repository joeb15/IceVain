package engine.render.renderers;

import engine.render.shaders.WorldShader;
import engine.render.textures.Texture;
import engine.utils.Camera;
import engine.world.Chunk;
import engine.world.World;
import engine.world.blocks.Block;
import org.joml.Vector3f;

import static engine.world.Chunk.CHUNK_HEIGHT;
import static engine.world.Chunk.CHUNK_SIZE;
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
        shader.loadTexture();
        shader.loadProjectionMatrix(camera);
        shader.loadViewMatrix(camera);
        shader.loadAmbient(new Vector3f(.1f,.1f,.1f));
    }

    /**
     * Renders the world
     */
    public void renderWorld(){
        glBindVertexArray(Block.CUBE_MODEL.getVaoId());
        Texture.getTexture("/resources/grass.jpg").bind(0);
        for (int c : shader.attribs)
            glEnableVertexAttribArray(c);
        for(int i=-2;i<=2;i++){
            for(int j=-2;j<=2;j++) {
                int k=0;
                Chunk chunk = world.getChunk(i, k, j);
                if (chunk != null) {
                    for (int x = 0; x < CHUNK_SIZE; x++) {
                        for (int z = 0; z < CHUNK_SIZE; z++) {
                            for (int y = 0; y < CHUNK_HEIGHT; y++) {
                                if (chunk.shouldRender(x, y, z)) {
                                    shader.loadPosition(i * CHUNK_SIZE + x, y + k * CHUNK_HEIGHT, j * CHUNK_SIZE + z);
                                    glDrawElements(GL_TRIANGLES, Block.CUBE_MODEL.getVertexCount(), GL_UNSIGNED_INT, 0);
                                }
                            }
                        }
                    }
                }
            }
        }
        for (int c : shader.attribs)
            glDisableVertexAttribArray(c);
        glBindVertexArray(0);
    }

    /**
     * Frees all previously allocated memory
     */
    public void cleanUp(){
        shader.cleanUp();
    }

}
