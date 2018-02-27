package socket;

import engine.exceptions.FailedToConnectException;
import engine.sockets.SocketManager;
import engine.utils.Debug;
import engine.utils.events.EventHandler;

import java.io.*;
import java.net.Socket;

public class ClientSocket {

    public static final int NUM_TRIES = 10;
    private Thread readThread;
    private boolean running = true;

    private Socket connectionToTheServer = null;

    /**
     * The socket that will handle all of the client side interfacing between the server and the client
     * @param host The IP address of the host computer
     * @param port The port that is open on the host computer
     * @param socketManager the socketManager that is used to
     * @throws FailedToConnectException thrown if the client cannot connect after <code>NUM_TRIES</code> times
     */
    public ClientSocket(String host, int port, SocketManager socketManager) throws FailedToConnectException {
        int numTries=0;
        while(connectionToTheServer==null && numTries<NUM_TRIES) {
            try {
                connectionToTheServer = new Socket(host, port);
            } catch (IOException e) {
                connectionToTheServer = null;
                numTries++;
                Debug.error("Failed to connect to "+host+":"+port+" #"+numTries);
            }
        }
        if(connectionToTheServer == null)
            throw(new FailedToConnectException());
        socketManager.addConnection(0);
        try {
            OutputStream out = connectionToTheServer.getOutputStream();
            PrintStream ps = new PrintStream(out, true);
            InputStream in = connectionToTheServer.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            new Thread(() -> {
                while (running) {
                    if (socketManager.hasMessage()) {
                        String message = socketManager.getMessage(0);
                        ps.println(message);
                    } else {
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, "ClientWrite").start();
            readThread = new Thread("ClientRead"){
                public void run() {
                    try {
                        while (running) {
                            if(in.available()>0) {
                                String text = br.readLine();
                                socketManager.onReceive(text, 0);
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
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            readThread.start();
            EventHandler.addEventCallback("cleanUp", (evt)->{
                running=false;
                readThread.interrupt();
            });
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Disconnects the client from the server
     */
    public void disconnect() {
        if(connectionToTheServer.isConnected()) {
            try {
                connectionToTheServer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
