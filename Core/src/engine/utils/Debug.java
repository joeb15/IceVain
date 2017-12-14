package engine.utils;

public class Debug {

    /**
     * Outputs a message to the stream
     *
     * @param   message The message to be displayed
     */
    public static void log(Object message){
        System.out.println(message.toString());
    }

    /**
     * Outputs a message to the error stream
     *
     * @param   message The error message to be displayed
     */
    public static void error(Object message){
        System.err.println(message.toString());
    }
}
