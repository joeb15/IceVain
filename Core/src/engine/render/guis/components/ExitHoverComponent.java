package engine.render.guis.components;

import org.joml.Vector2f;

public abstract class ExitHoverComponent extends GuiComponent{
    /**
     * A component that triggers upon exiting the area
     *
     * @param pos The position of the component
     * @param size The size of the component
     */
    public ExitHoverComponent(Vector2f pos, Vector2f size) {
        super(pos, size);
    }

    /**
     * The callback function that will be called upon exiting the component
     *
     * @param pos The position of the mouse
     * @param lastPos The last position of the mouse
     */
    public abstract void onExitHover(Vector2f pos, Vector2f lastPos);
}
