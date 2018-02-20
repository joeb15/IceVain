package engine.sockets;

public interface SocketInterface {
    void onReceive(String message, int socketNum);
}
