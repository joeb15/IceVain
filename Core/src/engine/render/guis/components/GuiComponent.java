package engine.render.guis.components;

import org.joml.Vector2f;

public abstract class GuiComponent {

    private Vector2f pos, size;

    /**
     * Represents a generalized component
     *
     * @param pos The position of the component
     * @param size The size of the component
     */
    public GuiComponent(Vector2f pos, Vector2f size) {
        this.pos = pos;
        this.size = size;
    }

    /**
     * Getter method for the position
     *
     * @return The current position of the component
     */
    public Vector2f getPos() {
        return pos;
    }

    /**
     * Setter for the position
     *
     * @param pos The new position of the component
     */
    public void setPos(Vector2f pos) {
        this.pos = pos;
    }

    /**
     * Getter for the size of the component
     *
     * @return The current size of the component
     */
    public Vector2f getSize() {
        return size;
    }

    /**
     * Setter for the size of the component
     *
     * @param size The new size of the component
     */
    public void setSize(Vector2f size) {
        this.size = size;
    }
}
