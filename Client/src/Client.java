import engine.render.Window;
import engine.utils.*;

import static engine.utils.GlobalVars.CFG_FPS_MAX;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Client {

    private int fps=0,tps=0;

    private Window window;

    public Client(){
        initialize();

        Debug.error("Config file has been loaded from:");
        Debug.error(VFS.getFile(GlobalVars.CONFIG_FILE).getPath());

        Timer.createTimer(()->{tps++;tick();}, 1000.0/60, -1);
        Timer.createTimer(()->{fps++;render();}, 1000.0/Config.getInt(CFG_FPS_MAX), -1);
        Timer.createTimer(()->{
            Debug.log("FPS:"+fps+" TPS:"+tps);
            fps=0;tps=0;
        }, 1000, -1);

        int i=0;
        while(!window.shouldClose()){
            Timer.tick();
        }

        cleanUp();
    }

    private void render(){
        glClear(GL_COLOR_BUFFER_BIT);

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
        glEnable(GL_TEXTURE_2D);
        window = new Window();

    }

    private void cleanUp(){
        glfwTerminate();
    }

    public static void main(String args[]){
        new Client();
    }
}
