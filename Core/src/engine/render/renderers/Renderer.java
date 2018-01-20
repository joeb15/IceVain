package engine.render.renderers;

import engine.entities.Entity;
import engine.physics.World;
import engine.render.models.RawModel;
import engine.render.models.TexturedModel;
import engine.render.shaders.DefaultShader;
import engine.utils.Camera;

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

    public Renderer(World world, Camera camera){
        this.camera = camera;
        shader = new DefaultShader();
        this.world=world;
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
//        model = Loader.loadOBJ(VFS.getFile("/lowpolytree.obj"));
    }

    public void render(){
        prepare();
        renderEntities();
        shader.unbind();
    }

    public void prepare(){
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        entityHashMap.clear();
        for(Entity e:world.getEntities())
            addEntity(e);
        shader.bind();
    }

    public void addEntity(Entity entity){
        TexturedModel texturedModel = entity.getTexturedModel();
        if(!entityHashMap.containsKey(texturedModel))
            entityHashMap.put(texturedModel, new ArrayList<>());
        entityHashMap.get(texturedModel).add(entity);
    }

    public void renderEntities(){
        shader.loadViewMatrix(camera);
        shader.loadTexture();

        for(TexturedModel texturedModel : entityHashMap.keySet()){
            RawModel model = texturedModel.getModel();
            glBindVertexArray(model.getVaoId());
            glEnableVertexAttribArray(0);
            glEnableVertexAttribArray(1);
            texturedModel.getTexture().getTexture().bind(0);
            for(Entity entity:entityHashMap.get(texturedModel)) {
                shader.loadTransformationMatrix(entity.getTransformationMatrix());
                glDrawElements(GL_TRIANGLES, model.getVertexCount(), GL_UNSIGNED_INT, 0);
            }
            glDisableVertexAttribArray(1);
            glDisableVertexAttribArray(0);
            glBindVertexArray(0);
        }

    }

    public void cleanUp(){
        shader.cleanUp();
    }

}
