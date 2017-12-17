package engine.utils;

public class GlobalVars {
    public static final String GAME_FOLDER = "C:\\Program Files\\Kore Studios\\Ice Vain\\";
    public static final String CONFIG_FILE = "/config.cfg";

    public static final String GAME_NAME = "Ice Vain";
    public static final int VERSION_MAJOR = 0;
    public static final int VERSION_MINOR = 0;
    public static final int VERSION_MICRO = 1;

    /**
     *  A list of config values and their names
     *  Meant for you to be able to tab through the list of config values without memorizing the names
     */
    public static final String CFG_FPS_MAX = "max_fps";
    public static final String CFG_FRAME_WIDTH = "window_width";
    public static final String CFG_FRAME_HEIGHT = "window_height";
    public static final String CFG_FRAME_FULLSCREEN = "window_fullscreen";

    public static final String CFG_CONTROL_FORWARD = "control_forward";
    public static final String CFG_CONTROL_BACK = "control_back";
    public static final String CFG_CONTROL_LEFT = "control_left";
    public static final String CFG_CONTROL_RIGHT = "control_right";
    public static final String CFG_CONTROL_EXIT = "control_exit";

    /**
     * A simple function to combine the game's name and version
     *
     * @return The full game name with major minor and micro version
     */
    public static String getGameNameAndVersion(){
        return GAME_NAME+" "+VERSION_MAJOR+"."+VERSION_MINOR+"."+VERSION_MICRO;
    }
}
