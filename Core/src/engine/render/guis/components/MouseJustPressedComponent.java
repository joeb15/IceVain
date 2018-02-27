package engine.render.guis.components;

import org.joml.Vector2f;

public abstract class MouseJustPressedComponent extends GuiComponent{

    private int mouseButton;

    /**
     * A component that will trigger upon the mouse just being pressed down
     *
     * @param pos The position of the component
     * @param size The size of the component
     * @param mouseButton The mouse button to watch for
     */
    public MouseJustPressedComponent(Vector2f pos, Vector2f size, int mouseButton) {
        super(pos, size);
        this.mouseButton = mouseButton;
    }

    /**
     * The method that will be called upon just pressing the component
     *
     * @param pos The position of the mouse
     */
    public abstract void onPressed(Vector2f pos);

    /**
     * Setter for the mouse button
     *
     * @param mouseButton The new mouse button to be set
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
