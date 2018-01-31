package engine.render.guis.components;

import org.joml.Vector2f;

public abstract class EnterHoverComponent extends GuiComponent{
    public EnterHoverComponent(Vector2f pos, Vector2f size) {
        super(pos, size);
    }

    public abstract void onEnterHover(Vector2f pos, Vector2f lastPos);
}
