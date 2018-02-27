package engine.render.guis.components;

import org.joml.Vector2f;

public abstract class MouseReleasedComponent extends GuiComponent{

    private int mouseButton;

    /**
     * A component that is triggered when the mouse is not being pressed
     *
     * @param pos The position of the component
     * @param size The size of the component
     * @param mouseButton The mouse button to watch
     */
    public MouseReleasedComponent(Vector2f pos, Vector2f size, int mouseButton) {
        super(pos, size);
        this.mouseButton = mouseButton;
    }

    /**
     * A method to call back when the mouse is not pressed above the component
     *
     * @param pos The position of the mouse
     */
    public abstract void onReleased(Vector2f pos);

    /**
     * Setter for the mouse button
     *
     * @param mouseButton The new mouse button to watch
     */
    public void setMouseButton(int mouseButton){
        this.mouseButton = mouseButton;
    }

    /**
     * Getter for the mouse button
     *
     * @return The current mouse button being watched
     */
    public int getMouseButton() {
        return mouseButton;
    }
}
