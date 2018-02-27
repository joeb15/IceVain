package events;

import engine.utils.events.Event;

import java.net.Socket;

public class ClientDisconnectedEvent extends Event {

    private Socket clientSocket;
    private int socketID;

    /**
     * Event to be sent out when the server disconnects from a client
     *
     * @param clientSocket The socket of the client
     * @param socketID The id of the client
     */
    public ClientDisconnectedEvent(Socket clientSocket, int socketID) {
        this.clientSocket = clientSocket;
        this.socketID=socketID;
    }

    /**
     * Getter for the client socket
     *
     * @return The socket connected to the client
     */
    public Socket getClientSocket() {
        return clientSocket;
    }

    /**
     * Getter for the socket's ID
     *
     * @return The ID of the socket
     */
    public int getSocketID() {
        return socketID;
    }
}
