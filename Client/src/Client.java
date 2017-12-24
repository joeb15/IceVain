import engine.render.Texture;
import engine.render.Window;
import engine.render.shaders.Shader;
import engine.utils.*;
import engine.utils.modelLoader.MaterialModelGroup;
import engine.utils.modelLoader.OBJLoader;

import static engine.utils.GlobalVars.CFG_FPS_MAX;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Client {

    private int fps=0,tps=0;

    private Window window;
    private static MaterialModelGroup model;
    private static Texture texture;
    private static Shader shader;

    public Client(){
        initialize();

        Debug.error("Config file has been loaded from:");
        Debug.error(VFS.getFile(GlobalVars.CONFIG_FILE).getPath());

        Timer.createTimer(()->{tps++;tick();}, 1000.0/60, -1);
        Timer.createTimer(()->{fps++;render();}, 1000.0/Config.getInt(CFG_FPS_MAX), -1);
        Timer.createTimer(()->{Debug.log("FPS:"+fps+" TPS:"+tps);fps=0;tps=0;}, 1000, -1);

        while(!window.shouldClose()){
            Timer.tick();
        }

        cleanUp();
    }

    /**
     * The main render loop of the game
     */
    private void render(){
        glClear(GL_COLOR_BUFFER_BIT);
        shader.bind();
        model.render();
        shader.unbind();
        window.swapBuffers();
    }

    /**
     * The main logic loop of the game
     */
    private void tick(){
        glfwPollEvents();
        Keyboard.tick();
    }

    /**
     * Initializes all of the variables and openGL and GLFW methods
     */
    private void initialize(){
        VFS.initializeVirtualSystems();
        Keyboard.initializeKeyboardConfig();
        if(!glfwInit()){
            throw new IllegalStateException("Failed to initialize GLFW");
        }
        window = new Window();

        glEnable(GL_TEXTURE_2D);

        texture = new Texture("/resources/muffin.jpg");
        shader = new Shader(VFS.getFile("/shaders/default.vert"),VFS.getFile("/shaders/default.frag"));
        model = OBJLoader.loadOBJ(VFS.getFile("/lowpolytree.obj"));

    }

    /**
     * Frees all the bindings from openGL
     */
    private void cleanUp(){
        glfwTerminate();
    }

    public static void main(String args[]){
        new Client();
    }
}
