package engine.render.guis.components;

import org.joml.Vector2f;

public abstract class EnterHoverComponent extends GuiComponent{
    /**
     * A component that will trigger when the mouse hovers over the area
     *
     * @param pos The position of the component
     * @param size The size of the component
     */
    public EnterHoverComponent(Vector2f pos, Vector2f size) {
        super(pos, size);
    }

    /**
     * The callback function that will be called when entering the component
     *
     * @param pos The position of the mouse
     * @param lastPos The last position of the mouse
     */
    public abstract void onEnterHover(Vector2f pos, Vector2f lastPos);
}
