package engine.render.guis.components;

import org.joml.Vector2f;

public abstract class ClickComponent extends GuiComponent{

    private int mouseButton;

    /**
     * A component that will trigger when a component has been clicked
     *
     * @param pos The position of the component
     * @param size The size of the component
     * @param mouseButton The mouse button to use
     */
    public ClickComponent(Vector2f pos, Vector2f size, int mouseButton) {
        super(pos, size);
        this.mouseButton = mouseButton;
    }

    /**
     * The callback to be called when the
     *
     * @param pos The position of where the component was clicked
     */
    public abstract void onClick(Vector2f pos);

    /**
     * Setter for the mouse button
     *
     * @param mouseButton The new mouse button to use
     */
    public void setMouseButton(int mouseButton){
        this.mouseButton = mouseButton;
    }

    /**
     * Getter for the mouse button
     *
     * @return The mouse button currently being used
     */
    public int getMouseButton() {
        return mouseButton;
    }
}
