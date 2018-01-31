package engine.utils;

import engine.render.guis.components.GuiComponent;
import org.joml.Vector2f;

public class Utils {

    public static boolean contains(Vector2f pos, Vector2f size, Vector2f point){
        return point.x>=pos.x && point.y>=pos.y&&point.x<=pos.x+size.x&&point.y<=pos.y+size.y;
    }

    public static boolean contains(GuiComponent gc, Vector2f point){
        return contains(gc.getPos(), gc.getSize(), point);
    }

}
