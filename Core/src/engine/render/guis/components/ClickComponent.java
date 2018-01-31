package engine.render.guis.components;

public interface ClickComponent extends GuiComponent{
    void onClick(boolean[] buttons, float mouseX, float mouseY);
}
