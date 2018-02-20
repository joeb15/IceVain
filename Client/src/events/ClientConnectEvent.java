package events;

import engine.utils.events.Event;
import socket.ClientSocket;

public class ClientConnectEvent extends Event {

    private ClientSocket clientSocket;

    public ClientConnectEvent(ClientSocket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public ClientSocket getClientSocket() {
        return clientSocket;
    }
}
