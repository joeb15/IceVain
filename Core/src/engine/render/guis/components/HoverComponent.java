package engine.render.guis.components;

import org.joml.Vector2f;

public abstract class HoverComponent extends GuiComponent{
    public HoverComponent(Vector2f pos, Vector2f size) {
        super(pos, size);
    }

    public abstract void onHover(Vector2f pos);
}
