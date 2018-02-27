package engine.render.guis.components;

import org.joml.Vector2f;

public abstract class HoverComponent extends GuiComponent{
    /**
     * Component that is called upon hovering over the component
     *
     * @param pos The position of the component
     * @param size The size of the component
     */
    public HoverComponent(Vector2f pos, Vector2f size) {
        super(pos, size);
    }

    /**
     * The callback method that is called when the component is hovered over
     *
     * @param pos The position of the mouse
     */
    public abstract void onHover(Vector2f pos);
}
