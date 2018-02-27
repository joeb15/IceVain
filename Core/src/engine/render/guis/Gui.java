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

    /**
     * A class to represent a graphical user interface that the user can interact with in 2D space
     *
     * @param texture The texture to display
     * @param x The x position of the gui
     * @param y The y position of the gui
     * @param w The width of the gui
     * @param h The height of the gui
     */
    public Gui(Texture texture, float x, float y, float w, float h) {
        this.texture = texture;
        pos = new Vector2f(x, y);
        size = new Vector2f(w, h);
        calculateMatrix();
    }

    /**
     * Recalculates the transformation matrix associated with the gui's position
     */
    private void calculateMatrix() {
        transformationMatrix.identity();
        transformationMatrix.translate(pos.x, pos.y, 0);
        transformationMatrix.scale(size.x, size.y, 0);
    }

    /**
     * Adds a component to the gui
     *
     * @param component The component to add
     */
    public void addComponent(GuiComponent component){
        components.add(component);
    }

    /**
     * Getter for the list of components associated with the gui
     *
     * @return The <code>ArrayList</code> representation of the GuiComponents
     */
    public ArrayList<GuiComponent> getComponents() {
        return components;
    }

    /**
     * Getter for the RawModel associated with a rectangle
     *
     * @return The rectangular <code>RawModel</code>
     */
    public static RawModel getRect(){
        return rect;
    }

    /**
     * Getter for the transformation matrix associated with the gui's position
     *
     * @return The <code>Matrix4f</code> representation of the transformation matrix
     */
    public Matrix4f getTransformationMatrix() {
        return transformationMatrix;
    }

    /**
     * Getter for the texture being used by the gui
     *
     * @return The Texture being used by the gui
     */
    public Texture getTexture() {
        return texture;
    }

    /**
     * Getter for the position of the gui
     *
     * @return The current position of the gui
     */
    public Vector2f getPos() {
        return pos;
    }

    /**
     * Getter for the size of the gui
     *
     * @return The current size of the component
     */
    public Vector2f getSize(){
        return size;
    }

    /**
     * Setter for the position of the gui
     *
     * @param pos The new position of the gui
     */
    public void setPos(Vector2f pos) {
        this.pos = pos;
        calculateMatrix();
    }

    /**
     * Setter for the size of the gui
     *
     * @param size The new size of the gui
     */
    public void setSize(Vector2f size) {
        this.size = size;
        calculateMatrix();
    }

    /**
     * Setter for the texture of the gui
     *
     * @param texture The new texture to be used for the gui
     */
    public void setTexture(Texture texture) {
        this.texture = texture;
    }
}
