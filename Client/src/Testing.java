import engine.render.Model;
import engine.render.Texture;
import engine.render.Window;
import engine.utils.Keyboard;
import engine.utils.VFS;
import engine.utils.modelLoader.OBJLoader;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Testing {

    private int fps=0,tps=0;

    private Window window;
    private static Model model;
    private static Texture texture;

    public Testing(){
        initialize();

        OBJLoader.loadOBJ(VFS.getFile("/lowpolytree.obj"));

//        Timer.createTimer(()->{tps++;tick();}, 1000.0/60, -1);
//        Timer.createTimer(()->{fps++;render();}, 1000.0/ Config.getInt(CFG_FPS_MAX), -1);
//        Timer.createTimer(()->{Debug.log("FPS:"+fps+" TPS:"+tps);fps=0;tps=0;}, 1000, -1);
//
//        while(!window.shouldClose()){
//            Timer.tick();
//        }

        cleanUp();
    }

    /**
     * The main render loop of the game
     */
    private void render(){
        glClear(GL_COLOR_BUFFER_BIT);
        texture.bind();
        model.bind();
        model.render();
        model.unbind();
        texture.unbind();
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
        model = new Model();

    }

    /**
     * Frees all the bindings from openGL
     */
    private void cleanUp(){
        glfwTerminate();
    }

    public static void main(String args[]){
        new Testing();
    }
}
