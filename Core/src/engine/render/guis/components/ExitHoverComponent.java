package engine.render.guis.components;

import org.joml.Vector2f;

public abstract class ExitHoverComponent extends GuiComponent{
    public ExitHoverComponent(Vector2f pos, Vector2f size) {
        super(pos, size);
    }

    public abstract void onExitHover(Vector2f pos, Vector2f lastPos);
}
