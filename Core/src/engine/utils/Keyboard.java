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

    public static void focus(Window window){
        Keyboard.window=window;
    }

    public static void tick(){
        for(int key:keyCallbacks.keySet()){
            boolean down = window.isKeyDown(key);
            boolean wasDown = keyValues.get(key)[0];
            keyValues.get(key)[0]=down;
            ArrayList<KeyCallback> callbacks = keyCallbacks.get(key);
            for(KeyCallback keyCallback:callbacks){
                keyCallback.keyEvent(down, wasDown);
            }
        }
    }

    /**
     * Add a key callback for the given letter on the keyboard
     *
     * @param key The character representation of the key you want
     * @param callback The callback for your key
     */
    public static void addKeyCallback(char key, KeyCallback callback) {
        addKeyCallback((int)(((key+"").toUpperCase()).charAt(0)), callback);
    }

    public static void addKeyCallback(int key, KeyCallback callback){
        if(!keyCallbacks.containsKey(key)) {
            keyCallbacks.put(key, new ArrayList<>());
            keyValues.put(key, new boolean[]{false});
        }
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
            addKeyCallback(key.charAt(0), callback);
        }else if(key.equals("ESC")){
            addKeyCallback(GLFW_KEY_ESCAPE, callback);
        }
    }
}
