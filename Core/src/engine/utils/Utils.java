package engine.utils;

import engine.render.guis.components.GuiComponent;
import org.joml.Vector2f;

public class Utils {

    /**
     * Returns whether a point is contained within an area
     *
     * @param pos The lower-left corner of the rectangle
     * @param size The size of the rectangle
     * @param point The point to test if it is contained
     * @return Whether the point is contained within a rectangle
     */
    public static boolean contains(Vector2f pos, Vector2f size, Vector2f point){
        return point.x>=pos.x && point.y>=pos.y&&point.x<=pos.x+size.x&&point.y<=pos.y+size.y;
    }

    /**
     * Returns whether a point is contained within a gui component
     *
     * @param gc The gui component to test
     * @param point The point to test if it is contained
     * @return Whether the point is contained within a rectangle
     */
    public static boolean contains(GuiComponent gc, Vector2f point){
        return contains(gc.getPos(), gc.getSize(), point);
    }

    /**
     * Rounds a number to an int
     *
     * @param val The value to round
     * @return The rounded value
     */
    public static int round(float val){
        return (int)(val+.5f);
    }

    /**
     * Rounds a number to a certain number of decimal places
     *
     * @param val The value to round
     * @param decimal The number of decimal places to round to
     * @return The rounded value
     */
    public static float round(float val, int decimal){
        for(int i=0;i<decimal;i++)
            val*=10;
        val = (int)(val+.5f);
        for(int i=0;i<decimal;i++)
            val/=10;
        return val;
    }

}
