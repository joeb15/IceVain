package engine.sockets;

import java.util.ArrayList;
import java.util.HashMap;

public class SocketManager {

    private HashMap<Integer, ArrayList<SocketInterface>> socketInterfaces = new HashMap<>();

    private HashMap<Integer, ArrayList<String>> messageStack = new HashMap<>();

    private boolean running=true;

    public SocketManager(){

    }

    public void addInterface(int code, SocketInterface socketInterface){
        if(!socketInterfaces.containsKey(code))
            socketInterfaces.put(code, new ArrayList<>());
        socketInterfaces.get(code).add(socketInterface);
    }

    public void pushMessage(int code, String message){
        for(ArrayList al:messageStack.values()) {
            al.add(code + "/" + message);
        }
    }

    public int getStackSize(){
        return messageStack.size();
    }

    public boolean hasMessage(){
        return messageStack.size()>0;
    }

    public void addConnection(int socketID){
        messageStack.put(socketID, new ArrayList<>());
    }

    public void removeConnection(int socketID){
        messageStack.remove(socketID);
    }

    public String getMessage(int socketID){
        ArrayList<String> messages = messageStack.get(socketID);
        if(messages==null || messages.size()==0)
            return null;
        return messages.remove(0);
    }

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

    public boolean isRunning(){
        return running;
    }

    public void terminate() {
        running=false;
    }
}
