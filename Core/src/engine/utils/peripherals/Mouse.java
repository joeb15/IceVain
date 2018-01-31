package engine.utils.peripherals;

import engine.render.Window;
import org.joml.Vector2f;

import java.util.ArrayList;

public class Mouse {

    private static ArrayList<MouseCallback> mouseCallbacks = new ArrayList<>();
    private static Window window;
    private static boolean[] mouseDown = new boolean[8];
    private static boolean[] mouseWasDown = new boolean[8];
    private static Vector2f mouseLastPos=new Vector2f();
    private static Vector2f mousePos=new Vector2f();
    private static Vector2f[] mouseDownPos = new Vector2f[8];

    public static void focus(Window window){
        Mouse.window=window;
    }

    public static void tick(){
        mouseLastPos = mousePos;
        mousePos = window.getMousePos();
        for(int i=0;i<8;i++){
            mouseWasDown[i] = mouseDown[i];
            mouseDown[i] = window.isMouseButtonPressed(i);
            if(!mouseWasDown[i] && mouseDown[i]){
                mouseDownPos[i] = mousePos;
            }
        }
        for(MouseCallback mc:mouseCallbacks){
            mc.mouseEvent(mouseDown, mouseWasDown, mousePos, mouseLastPos, mouseDownPos);
        }
    }

    public static void addMouseCallback(MouseCallback mouseCallback){
        mouseCallbacks.add(mouseCallback);
    }

    public static boolean isPressed(int mouse){
        return  mouseDown[mouse];
    }

    public static boolean wasPressed(int mouse){
        return  mouseWasDown[mouse];
    }

    public static boolean justPressed(int mouse){
        return  mouseDown[mouse] && !mouseWasDown[mouse];
    }

    public static boolean justReleased(int mouse){
        return  !mouseDown[mouse] && mouseWasDown[mouse];
    }

}
