package engine.utils;

import com.sun.security.auth.module.NTSystem;

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
                    File f = getFile(GlobalVars.getString(newPath));
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
            virtualSystems.put(virtualPath, new ArrayList<>());
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
        NTSystem NTSystem = new NTSystem();
        VFS.createVFS(GlobalVars.GAME_FOLDER, "/");
        GlobalVars.writeVariable("USERNAME", NTSystem.getName());
        GlobalVars.writeVariable("COMPUTER_NAME", NTSystem.getDomain());

    }

    /**
     * Returns the parent folder of a file
     *
     * @param file The file to find the parent of
     * @return The parent folder as a <code>File</code>
     */
    public static File getParent(File file) {
        return new File(file.getParent());
    }

    /**
     * Gets a file within the same folder given the other file's name
     *
     * @param file The file that is in the directory
     * @param newFileName The file name of the file you are trying to find
     * @return The new file if it exists
     */
    public static File getFileInSameFolder(File file, String newFileName) {
        return new File(getParent(file)+"/"+newFileName);
    }

    /**
     * Gets all the files in a given folder
     *
     * @param file The folder to get the children of
     * @return The array of children files
     */
    public static File[] getFilesInFolder(File file){
        return file.listFiles();
    }

    /**
     * Gets all files within a folder containing a certain string
     *
     * @param folder The folder to search within
     * @param regex The string to search for
     * @return An array of <code>File</code> with all the files satisfying this regex
     */
    public static File[] getFilesWithString(File folder, String regex){
        File[] children = getFilesInFolder(folder);
        int num = 0;
        for(File f:children){
            if(f.getName().contains(regex)){
                num++;
            }
        }
        if(num==0)
            return null;
        File[] contains = new File[num];
        num=0;
        for(File f:children){
            if(f.getName().contains(regex)){
                contains[num] = f;
                num++;
            }
        }
        return contains;
    }

    /**
     * Returns all files within a folder with recursion
     *
     * @param folder The root folder to search in
     * @param regex The string to search for in files
     * @return The <code>File[]</code> that contains the string
     */
    public static File[] getFilesWithStringRecursive(File folder, String regex){
        File[] children = getFilesInFolder(folder);
        int num = 0;
        for(File f:children){
            if(f.isDirectory()){
                File[] arr = getFilesWithStringRecursive(f, regex);
                if(arr!=null)
                    num+=arr.length;
            }else {
                if (f.getName().contains(regex)) {
                    num++;
                }
            }
        }
        if(num==0)
            return null;
        File[] contains = new File[num];
        num=0;
        for(File f:children){
            if(f.isDirectory()){
                File[] arr = getFilesWithStringRecursive(f, regex);
                if(arr!=null)
                    for(File f2:arr){
                        contains[num++] = f2;
                    }
            }else {
                if (f.getName().contains(regex)) {
                    contains[num++] = f;
                }
            }
        }
        return contains;
    }
}
