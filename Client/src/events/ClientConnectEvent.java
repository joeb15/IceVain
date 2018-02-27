package events;

import engine.utils.events.Event;
import socket.ClientSocket;

public class ClientConnectEvent extends Event {

    private ClientSocket clientSocket;

    /**
     * The event that is sent out when the client connects to a server
     *
     * @param clientSocket the socket that represents the connection between the client and server
     */
    public ClientConnectEvent(ClientSocket clientSocket) {
        this.clientSocket = clientSocket;
    }

    /**
     * Getter method to get the client socket from the event
     *
     * @return the client socket associated with the event
     */
    public ClientSocket getClientSocket() {
        return clientSocket;
    }
}
