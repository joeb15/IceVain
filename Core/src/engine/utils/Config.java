package engine.utils;

import java.io.*;

public class Config {

    /**
     *  Sets the value of a key in the config file and returns the previous value stored.
     *
     * @param   name    The name of the variable in the config file
     * @param   value   The value to set <code>name</code> to in the config file
     * @return          The previous value of <code>name</code> in the config file
     */
    public static String set(String name, Object value){
        return set(name, value.toString());
    }

    /**
     *  Sets the value of a key in the config file and returns the previous value stored.
     *
     * @param   name    The name of the variable in the config file
     * @param   value   The value to set <code>name</code> to in the config file
     * @return          The previous value of <code>name</code> in the config file
     */
    public static String set(String name, String value){
        try {
            //Open file and find if value exists in the file
            BufferedReader reader = getReader(GlobalVars.CONFIG_FILE);

            StringBuilder stringBuilder = new StringBuilder();
            String previousVal = null;
            String line;
            boolean found=false;
            while((line = reader.readLine()) != null){
                //ignore comment lines
                if(!line.startsWith("#")) {
                    String[] parts = line.split("=");
                    String currName = parts[0].trim();
                    if (name.trim().equals(currName)) {
                        stringBuilder.append(currName);
                        stringBuilder.append(" = ");
                        stringBuilder.append(value);
                        stringBuilder.append('\n');
                        if(parts.length>1)
                            previousVal = parts[1].trim();
                        found=true;
                    }else{
                        stringBuilder.append(line);
                        stringBuilder.append('\n');
                    }
                }else{
                    stringBuilder.append(line);
                    stringBuilder.append('\n');
                }
            }
            if(!found){
                stringBuilder.append(name);
                stringBuilder.append(" = ");
                stringBuilder.append(value);
                stringBuilder.append('\n');
            }
            reader.close();

            //If value was not found existing, then add the value.
            BufferedWriter writer = getWriter(GlobalVars.CONFIG_FILE);
            writer.write(stringBuilder.toString());
            writer.close();
            return previousVal;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *  Retrieves the value of a key in the config file
     *
     * @param   name    The name of the config value you want to read
     * @return          The value of <code>name</code> in the config file
     */
    public static String getString(String name){
        try {
            BufferedReader bufferedReader = getReader(GlobalVars.CONFIG_FILE);
            String currLine;

            while ((currLine = bufferedReader.readLine()) != null) {
                String[] parts = currLine.split("=");
                String currName = parts[0].trim();
                if (name.trim().equals(currName)) {
                    return parts[1].trim();
                }
            }

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     *  Retrieves the value of a key in the config file
     *
     * @param   name    The name of the config value you want to read
     * @return          The value of <code>name</code> in the config file
     */
    public static int getInt(String name){
        return Integer.parseInt(getString(name));
    }

    /**
     *  Retrieves a <code>BufferedWriter</code> object for the given config File
     *
     * @param   cfgFile The file that will be converted to a writing stream
     * @return          A <code>BufferedWriter</code> object representing <code>cfgFile</code>
     */
    private static BufferedWriter getWriter(String cfgFile){
        return getWriter(VFS.getFile(cfgFile));
    }

    /**
     *  Retrieves a <code>BufferedWriter</code> object for the given config File
     *
     * @param   cfgFile The file that will be converted to a writing stream
     * @return          A <code>BufferedWriter</code> object representing <code>cfgFile</code>
     */
    private static BufferedWriter getWriter(File cfgFile){
        try {
            return new BufferedWriter(new FileWriter(cfgFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *  Retrieves a <code>BufferedReader</code> object for the given config File
     *
     * @param   cfgFile The file that will be converted to a reading stream
     * @return          A <code>BufferedReader</code> object representing <code>cfgFile</code>
     */
    private static BufferedReader getReader(String cfgFile){
        return getReader(VFS.getFile(cfgFile));
    }

    /**
     *  Retrieves a <code>BufferedReader</code> object for the given config File
     *
     * @param   cfgFile The file that will be converted to a reading stream
     * @return          A <code>BufferedReader</code> object representing <code>cfgFile</code>
     */
    private static BufferedReader getReader(File cfgFile){
        try {
            return new BufferedReader(new FileReader(cfgFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
