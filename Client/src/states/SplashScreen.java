package states;

import engine.exceptions.FailedToConnectException;
import engine.physics.World;
import engine.render.guis.Gui;
import engine.render.guis.GuiManager;
import engine.render.guis.components.ClickComponent;
import engine.render.guis.components.EnterHoverComponent;
import engine.render.guis.components.ExitHoverComponent;
import engine.render.textures.Texture;
import engine.sockets.SocketManager;
import engine.utils.Config;
import engine.utils.GlobalVars;
import engine.utils.events.Event;
import engine.utils.events.EventHandler;
import engine.utils.states.State;
import events.ClientConnectEvent;
import org.joml.Vector2f;
import socket.ClientSocket;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;

public class SplashScreen extends State {

    private GuiManager guiManager;
    private World world;
    private SocketManager socketManager;

    public SplashScreen(GuiManager guiManager, World world, SocketManager socketManager) {
        this.guiManager = guiManager;
        this.world=world;
        this.socketManager=socketManager;
    }

    public void onCreate() {
        addGuis();
    }

    public void onEvent(Event e) {

    }

    public void onDestroy() {
        guiManager.clearGuis();
    }

    private void addGuis() {
        Texture muffin = new Texture("/resources/muffin.jpg");
        Texture stall = new Texture("/resources/stallTexture.png");
        Gui testGui = new Gui(muffin, 10,10,300,300);

        testGui.addComponent(new ClickComponent(testGui.getPos(), testGui.getSize(), GLFW_MOUSE_BUTTON_1) {
            public void onClick(Vector2f pos) {
                stateManager.setState(new GameState(guiManager, world));
            }
        });

        testGui.addComponent(new EnterHoverComponent(testGui.getPos(), testGui.getSize()) {
            public void onEnterHover(Vector2f pos, Vector2f lastPos) {
                testGui.setTexture(stall);
            }
        });

        testGui.addComponent(new ExitHoverComponent(testGui.getPos(), testGui.getSize()) {
            public void onExitHover(Vector2f pos, Vector2f lastPos) {
                testGui.setTexture(muffin);
            }
        });

        Gui testGui2 = new Gui(muffin, Config.getInt(GlobalVars.CFG_FRAME_WIDTH)-310,10,300,300);
        testGui2.addComponent(new ClickComponent(testGui2.getPos(), testGui2.getSize(), GLFW_MOUSE_BUTTON_1) {
            public void onClick(Vector2f pos) {
                try {
                    ClientSocket clientSocket = new ClientSocket("localhost", 8888, socketManager);
                    EventHandler.onEvent("clientConnect", new ClientConnectEvent(clientSocket));
                    EventHandler.addEventCallback("cleanUp",(evt)->{
                        clientSocket.disconnect();
                    });
                } catch (FailedToConnectException e) {
                    System.err.println("Failed to connect to server");
                }
            }
        });

        Gui testGui3 = new Gui(muffin, (Config.getInt(GlobalVars.CFG_FRAME_WIDTH)-310)/2,10,300,300);
        testGui3.addComponent(new ClickComponent(testGui3.getPos(), testGui3.getSize(), GLFW_MOUSE_BUTTON_1) {
            public void onClick(Vector2f pos) {
                socketManager.pushMessage(01, "Hello Joe");
            }
        });

        guiManager.addGui(testGui);
        guiManager.addGui(testGui2);
        guiManager.addGui(testGui3);
    }
}
