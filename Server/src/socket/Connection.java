package socket;

import engine.sockets.SocketManager;
import engine.utils.GlobalVars;
import engine.utils.events.EventHandler;
import events.ClientConnectedEvent;
import events.ClientDisconnectedEvent;

import java.io.*;
import java.net.Socket;

public class Connection {

    private boolean running=true;
    private Thread serverReadThread;
    private long lastPinged;
    private Socket connectionToTheClient;

    /**
     * Creates an interface between the server and a client
     *
     * @param socketNumber The number of the socket
     * @param connectionToTheClient The socket connecting to the client
     * @param socketManager The socketManager to handle all of the sockets from the client
     */
    public Connection(int socketNumber, Socket connectionToTheClient, SocketManager socketManager) {
        EventHandler.onEvent("clientConnected", new ClientConnectedEvent(connectionToTheClient, socketNumber));
        socketManager.addConnection(socketNumber);
        lastPinged = System.currentTimeMillis();
        this.connectionToTheClient=connectionToTheClient;
        try {
            OutputStream out = connectionToTheClient.getOutputStream();
            PrintStream ps = new PrintStream(out, true);
            InputStream in = connectionToTheClient.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            new Thread(connectionToTheClient.getInetAddress() + "ServerWrite"){
                public void run() {
                    while (running) {
                        if (socketManager.hasMessage()) {
                            String message = socketManager.getMessage(socketNumber);
                            ps.println(message);
                        } else {
                            try {
                                Thread.sleep(1);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }.start();
            serverReadThread = new Thread(connectionToTheClient.getInetAddress() + "ServerRead"){
                public void run() {
                    try {
                        while (running) {
                            if(in.available()>0) {
                                String text = br.readLine();
                                socketManager.onReceive(text, socketNumber);
                            }else if(connectionToTheClient.isConnected() && System.currentTimeMillis() - lastPinged > GlobalVars.SOCKET_TIME_OUT_TIME){
                                terminate();
                            }else{
                                Thread.sleep(1);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        //Happens when you close the program while sleeping
                    }
                }
                public void interrupt() {
                    super.interrupt();
                    try {
                        EventHandler.onEvent("clientDisconnected", new ClientDisconnectedEvent(connectionToTheClient, socketNumber));
                        socketManager.removeConnection(socketNumber);
                        br.close();
                        in.close();
                        connectionToTheClient.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            serverReadThread.start();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Terminates the connection between the client and server
     */
    public void terminate(){
        serverReadThread.interrupt();
        running=false;
    }

    /**
     * Triggered on ping of a client to make sure they are all active
     */
    public void ping() {
        lastPinged = System.currentTimeMillis();
    }
}
