package engine.render.guis.components;

import org.joml.Vector2f;

public abstract class MouseJustReleasedComponent extends GuiComponent{

    private int mouseButton;

    /**
     * A component that will trigger upon just releasing the mouse
     *
     * @param pos The position of the component
     * @param size The size of the component
     * @param mouseButton The mouse button to watch
     */
    public MouseJustReleasedComponent(Vector2f pos, Vector2f size, int mouseButton) {
        super(pos, size);
        this.mouseButton = mouseButton;
    }

    /**
     * The method that will be called upon when the mouse button is just released
     *
     * @param pos The position of the mouse
     */
    public abstract void onReleased(Vector2f pos);

    /**
     * Setter method for the mouse button
     *
     * @param mouseButton The new mouse button to be watched
     */
    public void setMouseButton(int mouseButton){
        this.mouseButton = mouseButton;
    }

    /**
     * Getter method for the mouse button
     *
     * @return The current mouse button being watched
     */
    public int getMouseButton() {
        return mouseButton;
    }
}
