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
                    String newPath = path.replace(virtualPath, realPath);
                    File f = getFile(newPath);
                    if(f != null) {
                        return f;
                    }
                }
            }
        }
        URL url = classLoader.getResource(path);
        if(url==null){
            return null;
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

}
