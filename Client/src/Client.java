import engine.render.Model;
import engine.render.Texture;
import engine.render.Window;
import engine.utils.*;

import static engine.utils.GlobalVars.CFG_FPS_MAX;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Client {

    private int fps=0,tps=0;

    private Window window;
    private static Model model;
    private static Texture texture;

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

    private void render(){
        glClear(GL_COLOR_BUFFER_BIT);
        texture.bind();
        model.bind();
        model.render();
        model.unbind();
        texture.unbind();
        window.swapBuffers();
    }

    private void tick(){
        glfwPollEvents();
        Keyboard.tick();
    }

    private void initialize(){
        VFS.initializeVirtualSystems();
        Keyboard.initializeKeyboardConfig();
        if(!glfwInit()){
            throw new IllegalStateException("Failed to initialize GLFW");
        }
        window = new Window();

        glEnable(GL_TEXTURE_2D);

        texture = new Texture("/muffin.jpg");
        model = new Model();

    }

    private void cleanUp(){
        glfwTerminate();
    }

    public static void main(String args[]){
        new Client();
    }
}
