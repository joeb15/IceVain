package engine.utils;

public class GlobalVars {
    public static final String GAME_FOLDER = "C:\\Program Files\\Kore Studios\\Ice Vain\\";
    public static final String CONFIG_FILE = GAME_FOLDER+"config.cfg";

    public static final String GAME_NAME = "Ice Vain";
    public static final int VERSION_MAJOR = 0;
    public static final int VERSION_MINOR = 0;
    public static final int VERSION_MICRO = 1;

    /**
     * A simple function to combine the game's name and version
     *
     * @return The full game name with major minor and micro version
     */
    public static String getGameNameAndVersion(){
        return GAME_NAME+" "+VERSION_MAJOR+"."+VERSION_MINOR+"."+VERSION_MICRO;
    }
}
