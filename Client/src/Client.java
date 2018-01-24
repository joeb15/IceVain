import engine.entities.Entity;
import engine.physics.World;
import engine.render.Window;
import engine.render.models.OBJLoader;
import engine.render.models.RawModel;
import engine.render.models.TexturedModel;
import engine.render.renderers.MasterRenderer;
import engine.render.textures.ModelTexture;
import engine.render.textures.Texture;
import engine.utils.*;
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
    }

    /**
     * Adds random entities to the world for testing
     */
    private void addEntities(){
        ModelTexture modelTexture1 = new ModelTexture(new Texture("/resources/muffin.jpeg"));
        RawModel model1 = Loader.loadToVAO(
                new float[]{-.5f, .5f, 0,-.5f, -.5f, 0,.5f, -.5f, 0,.5f, .5f, 0},
                new float[]{0,0,0,1,1,1,1,0},
                new int[]{0,1,3,3,1,2});
        TexturedModel texturedModel1 = new TexturedModel(model1, modelTexture1);

        ModelTexture modelTexture2 = new ModelTexture(new Texture("/resources/stallTexture.png"));
        RawModel model2 = OBJLoader.loadModel("/resources/stall.obj");
        TexturedModel texturedModel2 = new TexturedModel(model2, modelTexture2);

        Entity stall = new Entity(texturedModel2, new Vector3f(), new Vector3f(), new Vector3f(1,1,1));

        world.addEntity(stall);
        Timer.createTimer(()->{
            stall.setRotation(0, Timer.getTime(), 0);
            stall.setPos(0, (float)(-2.5+Math.sin(Timer.getTime()*1.246)), -20);
            }, 1000/60f, -1);
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
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_DEPTH_TEST);
        world = new World();
        addEntities();
        renderer = new MasterRenderer(window, camera, world);
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
