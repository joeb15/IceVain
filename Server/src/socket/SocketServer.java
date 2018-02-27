package socket;

import engine.sockets.SocketManager;
import engine.utils.events.EventHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;

public class SocketServer {

    public boolean running = true;
    private HashMap<Integer, Connection> connections = new HashMap<>();
    private Thread connectToClientThread;
    private int socketID = 0;

    /**
     * The main server socket to handle all connections to clients
     *
     * @param port The port to host the server on
     * @param socketManager The socketManager to handle the socket connections
     */
    public SocketServer(int port, SocketManager socketManager) {
        connectToClientThread = new Thread("ConnectToClient"){

            private ServerSocket server;

            public void run() {
                try {
                    server = new ServerSocket(port);
                    while(running){
                        Socket connectionToTheClient = server.accept();
                        Connection connection = new Connection(socketID, connectionToTheClient, socketManager);
                        connections.put(socketID++, connection);
                    }
                }catch (SocketException e){
                    running=false;
                }catch (IOException e){
                    e.printStackTrace();
                }
            }

            public void interrupt() {
                super.interrupt();
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        connectToClientThread.start();
        EventHandler.addEventCallback("cleanUp", (evt)->{
            running=false;
            for(Connection c:connections.values())
                c.terminate();
            connectToClientThread.interrupt();
        });
    }

    /**
     * Pings all client connections
     *
     * @param socketNum The socket to ping
     */
    public void ping(int socketNum) {
        connections.get(socketNum).ping();
    }

    /**
     * Disconnects a certain socket
     *
     * @param socketNum The socket to disconnect
     */
    public void disconnect(int socketNum) {
        connections.get(socketNum).terminate();
    }
}
