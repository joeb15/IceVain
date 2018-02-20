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
