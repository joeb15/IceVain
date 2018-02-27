package engine.render.renderers;

import engine.entities.Entity;
import engine.render.models.RawModel;
import engine.render.models.TexturedModel;
import engine.render.shaders.DefaultShader;
import engine.render.textures.ModelTexture;
import engine.utils.Camera;
import engine.world.Chunk;
import engine.world.World;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class Renderer {

    private HashMap<TexturedModel, ArrayList<Entity>> entityHashMap = new HashMap<>();
    private DefaultShader shader;
    private Camera camera;
    private World world;

    /**
     * Renders the world and entities
     *
     * @param world The world to render
     * @param camera The camera to use for the view matrix
     */
    public Renderer(World world, Camera camera){
        this.camera = camera;
        shader = new DefaultShader();
        shader.bind();
        shader.loadProjectionMatrix(camera);
        shader.loadTexture();
        shader.unbind();
        this.world=world;
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
    }

    /**
     * Renders the world and entities
     */
    public void render(){
        prepare();
        renderWorld();
        renderEntities();
        shader.unbind();
    }

    /**
     * Prepares the shader and entity hash for rendering
     */
    public void prepare(){
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        entityHashMap.clear();
        for(Entity e:world.getEntities())
            addEntity(e);
        shader.bind();
        shader.loadViewMatrix(camera);
        shader.loadAmbient(new Vector3f(.1f,.1f,.1f));
        shader.loadLights(world.getLights());
    }

    /**
     * Processes an entity to be rendered
     *
     * @param entity The entity to be added to the render hash
     */
    public void addEntity(Entity entity){
        TexturedModel texturedModel = entity.getTexturedModel();
        if(!entityHashMap.containsKey(texturedModel))
            entityHashMap.put(texturedModel, new ArrayList<>());
        entityHashMap.get(texturedModel).add(entity);
    }

    /**
     * Renders the world
     */
    public void renderWorld(){
        ModelTexture modelTexture = ModelTexture.get("/resources/grass.jpg");
        modelTexture.getTexture().bind(0);
        shader.loadTransformationMatrix(new Matrix4f());
        shader.loadShineAttribs(10, .1f);
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
     * Renders the entities
     */
    public void renderEntities(){
        for(TexturedModel texturedModel : entityHashMap.keySet()){
            RawModel model = texturedModel.getModel();
            glBindVertexArray(model.getVaoId());
            for(int i:shader.attribs)
                glEnableVertexAttribArray(i);
            texturedModel.getTexture().getTexture().bind(0);
            shader.loadShineAttribs(texturedModel.getShine(), texturedModel.getReflectivity());
            for(Entity entity:entityHashMap.get(texturedModel)) {
                shader.loadTransformationMatrix(entity.getTransformationMatrix());
                glDrawElements(GL_TRIANGLES, model.getVertexCount(), GL_UNSIGNED_INT, 0);
            }
            for(int i:shader.attribs)
                glDisableVertexAttribArray(i);
            glBindVertexArray(0);
        }

    }

    /**
     * Frees all previously allocated memory
     */
    public void cleanUp(){
        shader.cleanUp();
    }

}
