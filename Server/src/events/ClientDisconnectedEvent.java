package events;

import engine.utils.events.Event;

import java.net.Socket;

public class ClientDisconnectedEvent extends Event {

    private Socket clientSocket;
    private int socketID;

    public ClientDisconnectedEvent(Socket clientSocket, int socketID) {
        this.clientSocket = clientSocket;
        this.socketID=socketID;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public int getSocketID() {
        return socketID;
    }
}
