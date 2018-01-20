import engine.entities.Entity;
import engine.physics.World;
import engine.render.Window;
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
        ModelTexture modelTexture = new ModelTexture(new Texture("/resources/muffin.jpeg"));
        RawModel model = Loader.loadToVAO(
                new float[]{-.5f, .5f, 0,-.5f, -.5f, 0,.5f, -.5f, 0,.5f, .5f, 0},
                new float[]{0,0,0,1,1,1,1,0},
                new int[]{0,1,3,3,1,2});
        TexturedModel texturedModel = new TexturedModel(model, modelTexture);
        for(int i=0;i<10;i++){
            world.addEntity(new Entity(texturedModel,
                    new Vector3f((float) (Math.random()*10-5),(float) (Math.random()*10-5),(float) (Math.random()*10-5)),
                    new Vector3f((float) (Math.random()*360),(float) (Math.random()*360),(float) (Math.random()*360)),
                    new Vector3f((float) (Math.random()*5),(float) (Math.random()*5),(float) (Math.random()*5))));
        }
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
    }

    /**
     * Frees all the bindings from openGL
     */
    private void cleanUp(){
        Loader.cleanUp();
        glfwTerminate();
    }

    public static void main(String args[]){
        new Client();
    }
}
