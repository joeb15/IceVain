package engine.utils;

import java.util.HashMap;

/**
 * A class to hold all of the variables that should be referenced across the project
 */
public class GlobalVars {

    private static HashMap<String, String> variableDefinitions = new HashMap<>();

    public static final String GAME_FOLDER = "C:/Users/~USERNAME~/AppData/Roaming/Kore Studios/Ice Vain/";
    public static final String CONFIG_FILE = "/config.cfg";

    public static final String GAME_NAME = "Ice Vain";
    public static final int VERSION_MAJOR = 0;
    public static final int VERSION_MINOR = 0;
    public static final int VERSION_MICRO = 5;


    /**
     * A list of socket values
     */
    public static final long SOCKET_TIME_OUT_TIME = 10000;
    public static final int SOCKET_PING = 0;
    public static final int SOCKET_BROADCAST = 1;
    public static final int SOCKET_LOGIN = 2;
    public static final int SOCKET_DISCONNECT = 3;

    /**
     *  A list of config values and their names
     *  Meant for you to be able to tab through the list of config values without memorizing the names
     */
    public static final String CFG_FPS_MAX = "max_fps";
    public static final String CFG_FRAME_WIDTH = "window_width";
    public static final String CFG_FRAME_HEIGHT = "window_height";
    public static final String CFG_FRAME_FULLSCREEN = "window_fullscreen";
    public static final String CFG_LOD_BIAS = "lod_bias";

    public static final String CFG_CONTROL_FORWARD = "control_forward";
    public static final String CFG_CONTROL_BACK = "control_back";
    public static final String CFG_CONTROL_LEFT = "control_left";
    public static final String CFG_CONTROL_RIGHT = "control_right";
    public static final String CFG_CONTROL_UP = "control_up";
    public static final String CFG_CONTROL_DOWN = "control_down";
    public static final String CFG_CONTROL_EXIT = "control_exit";
    public static final String CFG_CONTROL_LOOK_LEFT = "control_look_left";
    public static final String CFG_CONTROL_LOOK_RIGHT = "control_look_right";

    /**
     * A simple function to combine the game's name and version
     *
     * @return The full game name with major minor and micro version
     */
    public static String getGameNameAndVersion(){
        return GAME_NAME+" "+VERSION_MAJOR+"."+VERSION_MINOR+"."+VERSION_MICRO;
    }

    /**
     * Writes a variable to config file and global scope
     *
     * @param variable the variable name to write to file
     * @param value the value to set the variable as
     */
    public static void writeVariable(String variable, Object value){
        setVariable(variable, value);
        Config.set("~"+variable+"~", value.toString());
    }

    /**
     * Sets a global variable for the current session
     *
     * @param variable The variable name to set
     * @param value The value to set the variable as
     */
    public static void setVariable(String variable, Object value) {
        variableDefinitions.put(variable, value.toString());
    }

    /**
     * Sets a global variable if it does not exist in config file
     *
     * @param variable The variable name to set
     * @param value The value to set the variable as
     */
    public static void setVariableIfAbsent(String variable, String value) {
        String cfgValue = Config.getString("~"+variable+"~");
        if(cfgValue.equals(""))
            writeVariable(variable, value);
    }

    /**
     * Gets the value of a string with variable constants enabled
     *
     * @param string The string to convert to variable defs
     * @return The value of that string after variable changes
     */
    public static String getString(String string) {
        for(String variable:variableDefinitions.keySet()){
            string = string.replaceAll("~"+variable+"~", variableDefinitions.get(variable));
        }
        return string;
    }

    /**
     * Loads all variables from the configuration file
     */
    public static void loadAllVariables(){
        Config.loadVariables();
    }
}
