package engine.render.guis.components;

import org.joml.Vector2f;

public abstract class GuiComponent {

    private Vector2f pos, size;

    public GuiComponent(Vector2f pos, Vector2f size) {
        this.pos = pos;
        this.size = size;
    }

    public Vector2f getPos() {
        return pos;
    }

    public void setPos(Vector2f pos) {
        this.pos = pos;
    }

    public Vector2f getSize() {
        return size;
    }

    public void setSize(Vector2f size) {
        this.size = size;
    }
}
