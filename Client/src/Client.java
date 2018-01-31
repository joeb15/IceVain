import engine.entities.Entity;
import engine.entities.Light;
import engine.physics.World;
import engine.render.Window;
import engine.render.guis.Gui;
import engine.render.guis.GuiManager;
import engine.render.guis.components.ClickComponent;
import engine.render.guis.components.EnterHoverComponent;
import engine.render.guis.components.ExitHoverComponent;
import engine.render.models.OBJLoader;
import engine.render.models.RawModel;
import engine.render.models.TexturedModel;
import engine.render.renderers.MasterRenderer;
import engine.render.textures.ModelTexture;
import engine.render.textures.Texture;
import engine.utils.*;
import engine.utils.peripherals.Keyboard;
import engine.utils.peripherals.Mouse;
import org.joml.Vector2f;
import org.joml.Vector3f;

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
     * Adds random entities to the world for testing
     */
    private void addEntities(){
        ModelTexture modelTexture2 = new ModelTexture(new Texture("/resources/stallTexture.png"));
        RawModel model2 = OBJLoader.loadModel("/resources/stall.obj");
        TexturedModel texturedModel2 = new TexturedModel(model2, modelTexture2);

        Entity stall = new Entity(texturedModel2, new Vector3f(), new Vector3f(), new Vector3f(1,1,1));
        Entity stall2 = new Entity(texturedModel2, new Vector3f(), new Vector3f(), new Vector3f(1,1,1));

        world.addEntity(stall);
        world.addEntity(stall2);
        Timer.createTimer(()->{
            stall.setRotation(0, -Timer.getTime(), 0);
            stall.setPos(-6, (float)(-2.5+Math.sin(Timer.getTime()*1.246)), -20);
            stall2.setRotation(0, Timer.getTime(), 0);
            stall2.setPos(6, (float)(-2.5-Math.sin(Timer.getTime()*1.246)), -20);
        }, 1000/60f, -1);
        world.addLight(new Light(new Vector3f(0, 0, 0), new Vector3f(1,1,1), new Vector3f(1,-.1f,0)));
    }

    private void addGuis(){
        Texture muffin = new Texture("/resources/muffin.jpeg");
        Texture stall = new Texture("/resources/stallTexture.png");
        Gui testGui = new Gui(muffin, 10,10,100,100);
        testGui.addComponent(new ClickComponent(testGui.getPos(), testGui.getSize(), GLFW_MOUSE_BUTTON_1) {
            public void onClick(Vector2f pos) {
                Debug.log("Clicked");
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
        guiManager.addGui(testGui);
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
        addEntities();
        addGuis();
        renderer = new MasterRenderer(window, camera, world, guiManager);
        tick();
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
