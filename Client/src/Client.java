import engine.physics.World;
import engine.render.Window;
import engine.render.guis.GuiManager;
import engine.render.renderers.MasterRenderer;
import engine.utils.*;
import engine.utils.peripherals.Keyboard;
import engine.utils.peripherals.Mouse;
import engine.utils.states.StateManager;
import states.SplashScreen;

import static engine.utils.GlobalVars.CFG_FPS_MAX;
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

    public Client(){
        initialize();

        Timer.createTimer(()->{tps++;tick();}, 1000.0/120, -1);
        Timer.createTimer(()->{fps++;renderer.render();}, 1000.0/Config.getInt(CFG_FPS_MAX), -1);
        Timer.createTimer(()->{Debug.log("FPS:"+fps+" TPS:"+tps);fps=0;tps=0;}, 1000, -1);

        while(!window.shouldClose()){
            Timer.tick();
        }

        cleanUp();
    }

    /**
     * The main logic loop of the game
     */
    private void tick(){
        glfwPollEvents();
        Keyboard.tick();
        Mouse.tick();
        guiManager.handleComponents();
    }

    /**
     * Initializes all of the variables and openGL and GLFW methods
     */
    private void initialize(){
        VFS.initializeVirtualSystems();
        Debug.error("User ~USERNAME~ is playing on computer ~COMPUTER_NAME~");
        Debug.error("Loading game from: "+GlobalVars.GAME_FOLDER);
        GlobalVars.loadAllVariables();
        camera = new Camera();
        Keyboard.initializeKeyboardConfig(camera);
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
        stateManager = new StateManager(new SplashScreen(guiManager, world));
        renderer = new MasterRenderer(window, camera, world, guiManager);
    }

    /**
     * Frees all the bindings from openGL
     */
    private void cleanUp(){
        Loader.cleanUp();
        renderer.cleanUp();
        glfwTerminate();
    }

    public static void main(String args[]){
        new Client();
    }
}
