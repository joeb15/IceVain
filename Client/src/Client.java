import engine.physics.World;
import engine.render.Window;
import engine.render.guis.GuiManager;
import engine.render.renderers.MasterRenderer;
import engine.sockets.SocketManager;
import engine.utils.*;
import engine.utils.events.EventHandler;
import engine.utils.peripherals.Keyboard;
import engine.utils.peripherals.Mouse;
import engine.utils.states.StateManager;
import events.FpsTpsDisplayEvent;
import states.SplashScreen;

import static engine.utils.GlobalVars.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Client {

    private int fps=0,tps=0;

    private Window window;
    private Camera camera;
    private World world;
    private MasterRenderer renderer;
    private GuiManager guiManager;
    private StateManager stateManager;
    private SocketManager socketManager;

    public Client(){
        EventHandler.addEventCallback("cleanUp",(evt)->{
            socketManager.pushMessage(SOCKET_DISCONNECT,"bye");
            Timer.tick();
        });

        initialize();

        Timer.createTimer(()->{tps++;tick();}, 1000.0/120, -1);
        Timer.createTimer(()->{fps++;renderer.render();}, 1000.0/Config.getInt(CFG_FPS_MAX), -1);
        Timer.createTimer(()->{EventHandler.onEvent("fpsTpsDisplay", new FpsTpsDisplayEvent(fps, tps));fps=0;tps=0;}, 1000, -1);

        while(!window.shouldClose()){
            Timer.tick();
        }

        cleanUp();
    }

    /**
     * The main logic loop of the game
     */
    private void tick(){
        EventHandler.onEvent("tick");
        glfwPollEvents();
        Keyboard.tick();
        Mouse.tick();
        guiManager.handleComponents();
    }

    /**
     * Initializes all of the variables and openGL and GLFW methods
     */
    private void initialize(){
        camera = new Camera();
        Timer.runAsSideProcess(()->{
            VFS.initializeVirtualSystems();
            GlobalVars.loadAllVariables();
            Keyboard.initializeKeyboardConfig(camera);
        });
        if(!glfwInit()){
            throw new IllegalStateException("Failed to initialize GLFW");
        }
        window = new Window();
        Keyboard.focus(window);
        Mouse.focus(window);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_DEPTH_TEST);
        world = new World();
        guiManager = new GuiManager();
        socketManager = new SocketManager();
        socketManager.addInterface(SOCKET_PING,(msg, socketNum)-> socketManager.pushMessage(SOCKET_PING,"pong"));
        socketManager.addInterface(SOCKET_BROADCAST,(msg, socketNum)-> System.out.println(msg));
        stateManager = new StateManager(new SplashScreen(guiManager, world, socketManager));
        renderer = new MasterRenderer(window, camera, world, guiManager);
    }

    /**
     * Frees all the bindings from openGL
     */
    private void cleanUp(){
        EventHandler.onEvent("cleanUp");
        Loader.cleanUp();
        renderer.cleanUp();
        glfwTerminate();
    }

    public static void main(String args[]){
        new Client();
    }
}
