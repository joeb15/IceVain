package engine.utils;

import engine.render.Window;

import java.util.ArrayList;
import java.util.HashMap;

import static engine.utils.GlobalVars.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;

public class Keyboard {

    private static HashMap<Integer, ArrayList<KeyCallback>> keyCallbacks = new HashMap<>();
    private static HashMap<Integer, boolean[]> keyValues = new HashMap<>();
    private static Window window;
    private static boolean[] keyDown = new boolean[65535];
    private static boolean[] keyWasDown = new boolean[65535];

    /**
     *
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
            boolean down = window.isKeyDown(key);
            boolean wasDown = keyValues.get(key)[0];
            keyWasDown[key] = wasDown;
            keyDown[key] = down;
            keyValues.get(key)[0]=down;
            ArrayList<KeyCallback> callbacks = keyCallbacks.get(key);
            for(KeyCallback keyCallback:callbacks){
                keyCallback.keyEvent(down, wasDown);
            }
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
     */
    public static void initializeKeyboardConfig() {
        addKeyCallbackFromConfig(CFG_CONTROL_FORWARD, (d, wd)->{ if(d&&!wd)System.out.println('w'); });
        addKeyCallbackFromConfig(CFG_CONTROL_BACK, (d, wd)->{ if(d&&!wd)System.out.println('s'); });
        addKeyCallbackFromConfig(CFG_CONTROL_LEFT, (d, wd)->{ if(d&&!wd)System.out.println('a'); });
        addKeyCallbackFromConfig(CFG_CONTROL_RIGHT, (d, wd)->{ if(d&&!wd)System.out.println('d'); });
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
        }else if(key.equals("ESC") || key.equals("ESCAPE")){
            addKeyCallback(GLFW_KEY_ESCAPE, callback);
        }
    }
}
