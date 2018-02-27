package engine.sockets;

import java.util.ArrayList;
import java.util.HashMap;

public class SocketManager {

    private HashMap<Integer, ArrayList<SocketInterface>> socketInterfaces = new HashMap<>();

    private HashMap<Integer, ArrayList<String>> messageStack = new HashMap<>();

    private boolean running=true;

    /**
     * Adds a socket interface to the current socket handler
     *
     * @param code The interface code
     * @param socketInterface The socket interface to be added
     */
    public void addInterface(int code, SocketInterface socketInterface){
        if(!socketInterfaces.containsKey(code))
            socketInterfaces.put(code, new ArrayList<>());
        socketInterfaces.get(code).add(socketInterface);
    }

    /**
     * Pushes a message to all receiving sockets
     *
     * @param code The code of the message
     * @param message The actual message being sent
     */
    public void pushMessage(int code, String message){
        for(ArrayList<String> al:messageStack.values()) {
            if(al!=null)
                al.add(code + "/" + message);
        }
    }

    /**
     * Getter for the message stack size
     *
     * @return The amount of messages waiting on the stack
     */
    public int getStackSize(){
        return messageStack.size();
    }

    /**
     * Getter for if the stack has messages waiting
     *
     * @return Whether or not the stack has messages
     */
    public boolean hasMessage(){
        return messageStack.size()>0;
    }

    /**
     * Adds a connection to the current sockets
     *
     * @param socketID The id of the socket to be monitored
     */
    public void addConnection(int socketID){
        messageStack.put(socketID, new ArrayList<>());
    }

    /**
     * Removes a connection from the list of current sockets
     *
     * @param socketID The id of the socket to be unmonitored
     */
    public void removeConnection(int socketID){
        messageStack.remove(socketID);
    }

    /**
     * Gets the first message from the stack for the given socket
     *
     * @param socketID The socket id for the connection
     * @return The first message from the stack
     */
    public String getMessage(int socketID){
        ArrayList<String> messages = messageStack.get(socketID);
        if(messages==null || messages.size()==0)
            return null;
        return messages.remove(0);
    }

    /**
     * Handles the receiving of code regarding sockets
     *
     * @param message The message being received
     * @param socketNum The socket it is received from
     */
    public void onReceive(String message, int socketNum){
        int slashIndex = message.indexOf('/');
        if(slashIndex==-1){
            return;
        }
        int code = Integer.parseInt(message.substring(0, slashIndex));
        String msg = message.substring(slashIndex+1);
        for(SocketInterface socketInterface:socketInterfaces.get(code))
            socketInterface.onReceive(msg, socketNum);
    }

    /**
     * Gets whether or not the socket is running
     *
     * @return If the socket is running
     */
    public boolean isRunning(){
        return running;
    }

    /**
     * Ends the socket connection
     */
    public void terminate() {
        running=false;
    }
}
