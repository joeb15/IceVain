import engine.sockets.SocketManager;
import engine.utils.Debug;
import engine.utils.Timer;
import engine.utils.events.EventHandler;
import events.ClientConnectedEvent;
import events.ClientDisconnectedEvent;
import socket.SocketServer;

import static engine.utils.GlobalVars.*;

public class Server {

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args){
        SocketManager socketManager = new SocketManager();
        SocketServer socketServer = new SocketServer(8888, socketManager);

        socketManager.addInterface(SOCKET_PING, (msg, socketNum)-> socketServer.ping(socketNum));
        socketManager.addInterface(SOCKET_BROADCAST, (msg, socketNum)-> System.out.println(msg));
        socketManager.addInterface(SOCKET_DISCONNECT, (msg, socketNum)-> socketServer.disconnect(socketNum));

        Timer.createTimer(()-> socketManager.pushMessage(SOCKET_PING,"ping"),1000, -1);

        EventHandler.addEventCallback("clientConnected", (evt)->{
            ClientConnectedEvent clientConnectedEvent = (ClientConnectedEvent)evt;
            Debug.log(clientConnectedEvent.getClientSocket().getInetAddress()+":"+clientConnectedEvent.getSocketID()+" has connected");
        });
        EventHandler.addEventCallback("clientDisconnected", (evt)->{
            ClientDisconnectedEvent clientDisconnectedEvent = (ClientDisconnectedEvent)evt;
            Debug.log(clientDisconnectedEvent.getClientSocket().getInetAddress()+":"+clientDisconnectedEvent.getSocketID()+" has disconnected");
        });
        while(true){
            Timer.tick();
        }
    }

}
