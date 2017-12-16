package engine.render;

import engine.utils.Config;
import engine.utils.GlobalVars;
import org.lwjgl.glfw.GLFWVidMode;

import static engine.utils.GlobalVars.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;

public class Window {

    private long window;

    /**
     * A class to hold all of the methods and variables that surround a window
     */
    public Window(){
        long primary = glfwGetPrimaryMonitor();
        int width = Config.getInt(CFG_FRAME_WIDTH);
        int height = Config.getInt(CFG_FRAME_HEIGHT);

        if(Config.getInt(CFG_FRAME_FULLSCREEN)==1)
            window = glfwCreateWindow(width, height, GlobalVars.getGameNameAndVersion(), primary, 0);
        else
            window = glfwCreateWindow(width, height, GlobalVars.getGameNameAndVersion(), 0, 0);

        if(window == 0){
            throw new IllegalStateException("Failed to create window");
        }

        GLFWVidMode videoMode = glfwGetVideoMode(primary);
        glfwSetWindowPos(window, (videoMode.width()-width)/2, (videoMode.height()-height)/2);

        glfwShowWindow(window);

        glfwMakeContextCurrent(window);

        glfwSwapInterval(0);

        createCapabilities();
    }

    /**
     * Simple getter method to see if the window should close or not (The X has been clicked)
     *
     * @return Whether or not the window should close
     */
    public boolean shouldClose(){
        return glfwWindowShouldClose(window);
    }

    /**
     * Simple accessor method to swap the buffers for the window
     */
    public void swapBuffers() {
        glfwSwapBuffers(window);
    }
}
