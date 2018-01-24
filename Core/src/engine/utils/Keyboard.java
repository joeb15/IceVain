package engine.utils;

import engine.render.Window;

import java.util.ArrayList;
import java.util.HashMap;

import static engine.utils.GlobalVars.*;
import static org.lwjgl.glfw.GLFW.*;

public class Keyboard {

    private static HashMap<Integer, ArrayList<KeyCallback>> keyCallbacks = new HashMap<>();
    private static HashMap<Integer, boolean[]> keyValues = new HashMap<>();
    private static Window window;
    private static boolean[] keyDown = new boolean[65535];
    private static boolean[] keyWasDown = new boolean[65535];

    /**
     * Focuses the given window
     *
     * @param window The window to focus for the keystrokes
     */
    public static void focus(Window window){
        Keyboard.window=window;
    }

    /**
     * Checks all current keyCallbacks
     */
    public static void tick(){
        for(int key:keyCallbacks.keySet()){
            new Thread(()->{
                boolean down = window.isKeyDown(key);
                boolean wasDown = keyValues.get(key)[0];
                keyWasDown[key] = wasDown;
                keyDown[key] = down;
                keyValues.get(key)[0]=down;
                ArrayList<KeyCallback> callbacks = keyCallbacks.get(key);
                for(KeyCallback keyCallback:callbacks){
                    keyCallback.keyEvent(down, wasDown);
                }
            }).start();
        }
    }

    /**
     * Converts a character to a glfw key code
     * @param key The character you want to convert e.g. <code>'a'</code>
     * @return The keycode for the given character e.g. <code>GLFW.GLFW_KEY_A</code>
     */
    public static int getCodeFromChar(char key){
        return (int)(((key+"").toUpperCase()).charAt(0));
    }

    /**
     * Getter for whether a key is pressed or not
     * @param key the integer keycode for the given key e.g. <code>GLFW.GLFW_KEY_A</code>
     * @return Whether or not the key is pressed
     */
    public static boolean isPressed(int key){
        return  keyDown[key];
    }

    /**
     * Getter for whether a key was pressed or not
     * @param key the integer keycode for the given key e.g. <code>GLFW.GLFW_KEY_A</code>
     * @return Whether or not the key was pressed
     */
    public static boolean wasPressed(int key){
        return  keyWasDown[key];
    }

    /**
     * Getter for whether a key is just pressed or not
     * @param key the integer keycode for the given key e.g. <code>GLFW.GLFW_KEY_A</code>
     * @return Whether or not the key is just pressed
     */
    public static boolean justPressed(int key){
        return  keyDown[key] && !keyWasDown[key];
    }

    /**
     * Getter for whether a key was just released or not
     * @param key the integer keycode for the given key e.g. <code>GLFW.GLFW_KEY_A</code>
     * @return Whether or not the key was just released
     */
    public static boolean justReleased(int key){
        return  !keyDown[key] && keyWasDown[key];
    }

    /**
     * Listens for a keypress for the given key
     *
     * @param key The integer keycode for a keystroke e.g. <code>GLFW.GLFW_KEY_A</code>
     */
    public static void addKeyListener(int key){
        if(!keyCallbacks.containsKey(key)) {
            keyCallbacks.put(key, new ArrayList<>());
            keyValues.put(key, new boolean[]{false});
        }
    }

    /**
     * Add a key callback for the given letter on the keyboard
     *
     * @param key The character representation of the key you want
     * @param callback The callback for your key
     */
    public static void addKeyCallback(int key, KeyCallback callback){
        addKeyListener(key);
        keyCallbacks.get(key).add(callback);
    }

    /**
     * Initializes all of the basic keyboard shortcuts
     * @param camera
     */
    public static void initializeKeyboardConfig(Camera camera) {
        addKeyCallbackFromConfig(CFG_CONTROL_FORWARD, (d, wd)->{ if(d)camera.moveForward();});
        addKeyCallbackFromConfig(CFG_CONTROL_BACK, (d, wd)->{ if(d)camera.moveBackward();});
        addKeyCallbackFromConfig(CFG_CONTROL_LEFT, (d, wd)->{ if(d)camera.strafeLeft();});
        addKeyCallbackFromConfig(CFG_CONTROL_RIGHT, (d, wd)->{ if(d)camera.strafeRight();});
        addKeyCallbackFromConfig(CFG_CONTROL_UP, (d, wd)->{ if(d)camera.move(0,-camera.getSpeed(), 0);});
        addKeyCallbackFromConfig(CFG_CONTROL_DOWN, (d, wd)->{ if(d)camera.move(0,camera.getSpeed(), 0);});
        addKeyCallbackFromConfig(CFG_CONTROL_LOOK_LEFT, (d, wd)->{ if(d)camera.rotate(0,camera.getRotationSpeed(),0);});
        addKeyCallbackFromConfig(CFG_CONTROL_LOOK_RIGHT, (d, wd)->{ if(d)camera.rotate(0,-camera.getRotationSpeed(),0);});
        addKeyCallbackFromConfig(CFG_CONTROL_EXIT, (d, wd)->{ if(wd&&!d)window.close();});
    }

    /**
     * Adds key callbacks from the config file given their config name and the callback
     *
     * @param configName The name of the variable in the config file
     * @param callback The callback that will be called
     */
    public static void addKeyCallbackFromConfig(String configName, KeyCallback callback){
        String key = Config.getString(configName).toUpperCase();
        if(key.length()==1){
            addKeyCallback(getCodeFromChar(key.charAt(0)), callback);
        }else{
            switch (key){
                case "ESC":
                case "ESCAPE":
                    addKeyCallback(GLFW_KEY_ESCAPE, callback);break;
                case "SHIFT":
                    addKeyCallback(GLFW_KEY_LEFT_SHIFT, callback);
                    addKeyCallback(GLFW_KEY_RIGHT_SHIFT, callback);break;
                case "LEFT SHIFT":
                case "LSHIFT":
                    addKeyCallback(GLFW_KEY_LEFT_SHIFT, callback);break;
                case "RIGHT SHIFT":
                case "RSHIFT":
                    addKeyCallback(GLFW_KEY_RIGHT_SHIFT, callback);break;
                case "CTRL":
                case "CONTROL":
                    addKeyCallback(GLFW_KEY_LEFT_CONTROL, callback);
                    addKeyCallback(GLFW_KEY_RIGHT_CONTROL, callback);break;
                case "LCTRL":
                case "LCONTROL":
                case "LEFT CTRL":
                case "LEFT CONTROL":
                    addKeyCallback(GLFW_KEY_LEFT_CONTROL, callback);break;
                case "RCTRL":
                case "RCONTROL":
                case "RIGHT CTRL":
                case "RIGHT CONTROL":
                    addKeyCallback(GLFW_KEY_RIGHT_CONTROL, callback);break;
                case "SPACE":
                    addKeyCallback(GLFW_KEY_SPACE, callback);break;
            }
        }
    }
}
