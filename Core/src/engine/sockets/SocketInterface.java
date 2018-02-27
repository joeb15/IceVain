package engine.sockets;

/**
 * An interface for handling socket connections
 */
public interface SocketInterface {
    /**
     * The callback when a message is received
     *
     * @param message The message received
     * @param socketNum The socket being received from
     */
    void onReceive(String message, int socketNum);
}
