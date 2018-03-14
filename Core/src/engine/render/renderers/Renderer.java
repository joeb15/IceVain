package engine.render.renderers;

import engine.entities.Entity;
import engine.render.models.RawModel;
import engine.render.models.TexturedModel;
import engine.render.shaders.DefaultShader;
import engine.render.shaders.IDShader;
import engine.utils.Camera;
import engine.world.World;
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
    private IDShader idShader;
    private Camera camera;
    private World world;

    /**
     * Renders the  entities
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
        idShader = new IDShader();
        idShader.bind();
        idShader.loadProjectionMatrix(camera);
        idShader.unbind();
        this.world=world;
    }

    /**
     * Renders the world and entities
     */
    public void render(){
        prepare();
        renderEntities();
        shader.unbind();
    }

    /**
     * Prepares the shader and entity hash for rendering
     */
    public void prepare(){
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
     * Renders the entities
     */
    public void renderEntities(){
        for(TexturedModel texturedModel : entityHashMap.keySet()){
            RawModel model = texturedModel.getModel();
            glBindVertexArray(model.getVaoId());
            for(int i:shader.attribs)
                glEnableVertexAttribArray(i);
            shader.loadMaterialLibrary(texturedModel.getMaterialLibrary());
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
     * Renders the entities
     */
    public void renderEntityIDs(){
        idShader.bind();
        idShader.loadViewMatrix(camera);
        for(TexturedModel texturedModel : entityHashMap.keySet()){
            RawModel model = texturedModel.getModel();
            glBindVertexArray(model.getVaoId());
            for(int i:idShader.attribs)
                glEnableVertexAttribArray(i);
            for(Entity entity:entityHashMap.get(texturedModel)) {
                idShader.loadTransformationMatrix(entity.getTransformationMatrix());
                idShader.loadColor(new Vector3f((float)(entity.id%256)/255, (float)(entity.id/256%256)/255, (float)(entity.id/256/256%256)/255));
                glDrawElements(GL_TRIANGLES, model.getVertexCount(), GL_UNSIGNED_INT, 0);
            }
            for(int i:idShader.attribs)
                glDisableVertexAttribArray(i);
            glBindVertexArray(0);
        }
        idShader.unbind();
    }

    /**
     * Frees all previously allocated memory
     */
    public void cleanUp(){
        shader.cleanUp();
        idShader.cleanUp();
    }

}
