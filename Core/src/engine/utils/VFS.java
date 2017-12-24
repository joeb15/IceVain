package engine.utils;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class VFS {

    private static HashMap<String, ArrayList<String>> virtualSystems = new HashMap<>();

    /**
     *  Retrieves a file within the local or virtual filesystem
     *
     * @param   path    The local path of the file to load
     * @return          The file that was associated with that path
     */
    public static File getFile(String path){
        File absoluteFile = new File(path);
        if(absoluteFile.exists())
            return absoluteFile;
        ClassLoader classLoader = VFS.class.getClassLoader();
        for(String virtualPath:virtualSystems.keySet()){
            if(path.startsWith(virtualPath)){
                for(String realPath:virtualSystems.get(virtualPath)){
                    String newPath = path.replaceFirst(virtualPath, realPath);
                    File f = getFile(newPath);
                    if(f != null) {
                        return f;
                    }
                }
            }
        }
        URL url = classLoader.getResource(path);
        if(url==null){
            return absoluteFile;
        }
        return new File(url.getFile());
    }

    /**
     * Creates a virtual file system for the given folders
     *
     * @param   path            The actual path of the folder
     * @param   virtualPath     The virtual path of the folder
     */
    public static void createVFS(String path, String virtualPath){
        if(!virtualSystems.containsKey(virtualPath))
            virtualSystems.put(virtualPath, new ArrayList<String>());
        virtualSystems.get(virtualPath).add(path);
    }

    /**
     * Determines whether a file exists or not
     *
     * @param   path    The path of the file to be checked
     * @return          Whether or not the specified file exists within the virtual file system
     */
    public static boolean fileExists(String path){
        File f = getFile(path);
        return f!=null && f.exists() && !f.isDirectory();
    }

    /**
     * Determines whether a folder exists or not
     *
     * @param   path    The path of the folder to be checked
     * @return          Whether or not the specified folder exists within the virtual file system
     */
    public static boolean folderExists(String path){
        File f = getFile(path);
        return f!=null && f.exists() && f.isDirectory();
    }

    /**
     * Initializes the virtual systems that are built in to the game
     * Called upon startup
     */
    public static void initializeVirtualSystems() {
        VFS.createVFS(GlobalVars.GAME_FOLDER, "/");

    }
}
