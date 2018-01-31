package engine.render.guis;

import engine.render.guis.components.GuiComponent;
import engine.render.models.RawModel;
import engine.render.textures.Texture;
import engine.utils.Loader;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import java.util.ArrayList;

public class Gui {

    private static RawModel rect = Loader.loadToVAO(new float[]{0,1,0,1,1,0,0,0,0,1,0,0},new float[]{0,0,-1,0,0,-1,0,0,-1,0,0,-1},new float[]{0,0,1,0,0,1,1,1},new int[]{3,1,2,2,1,0});

    private Vector2f pos, size;
    private Matrix4f transformationMatrix = new Matrix4f();
    private Texture texture;
    private ArrayList<GuiComponent> components = new ArrayList<>();

    public Gui(Texture texture, float x, float y, float w, float h) {
        this.texture = texture;
        pos = new Vector2f(x, y);
        size = new Vector2f(w, h);
        calculateMatrix();
    }

    private void calculateMatrix() {
        transformationMatrix.identity();
        transformationMatrix.translate(pos.x, pos.y, 0);
        transformationMatrix.scale(size.x, size.y, 0);
    }

    public void addComponent(GuiComponent component){
        components.add(component);
    }

    public ArrayList<GuiComponent> getComponents() {
        return components;
    }

    public static RawModel getRect(){
        return rect;
    }

    public Matrix4f getTransformationMatrix() {
        return transformationMatrix;
    }

    public Texture getTexture() {
        return texture;
    }

    public Vector2f getPos() {
        return pos;
    }

    public Vector2f getSize(){
        return size;
    }
}
