package engine.render.guis.components;

import org.joml.Vector2f;

public abstract class MousePressedComponent extends GuiComponent{

    private int mouseButton;

    public MousePressedComponent(Vector2f pos, Vector2f size, int mouseButton) {
        super(pos, size);
        this.mouseButton = mouseButton;
    }

    public abstract void onPressed(Vector2f pos);

    public void setMouseButton(int mouseButton){
        this.mouseButton = mouseButton;
    }

    public int getMouseButton() {
        return mouseButton;
    }
}
