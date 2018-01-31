package engine.render;

import engine.utils.Config;
import engine.utils.GlobalVars;
import engine.utils.peripherals.Keyboard;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFWVidMode;

import static engine.utils.GlobalVars.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;

public class Window {

    private long window;
    private int width, height;
    /**
     * A class to hold all of the methods and variables that surround a window
     */
    public Window(){
        long primary = glfwGetPrimaryMonitor();
        width = Config.getInt(CFG_FRAME_WIDTH);
        height = Config.getInt(CFG_FRAME_HEIGHT);

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

        Keyboard.focus(this);
    }

    /**
     * Getter method to see if the window should close or not (The X has been clicked)
     *
     * @return Whether or not the window should close
     */
    public boolean shouldClose(){
        return glfwWindowShouldClose(window);
    }

    /**
     * Accessor method to swap the buffers for the window
     */
    public void swapBuffers() {
        glfwSwapBuffers(window);
    }

    /**
     * Accessor method to see if the key is pressed or not
     *
     * @param key The keycode
     * @return Whether or not the key is down or not
     */
    public boolean isKeyDown(int key) {
        return glfwGetKey(window, key)==GLFW_TRUE;
    }

    /**
     * Accessor method to close the window on request
     */
    public void close() {
        glfwSetWindowShouldClose(window, true);
    }

    public boolean isMouseButtonPressed(int button) {
        return glfwGetMouseButton(window, button) == GLFW_TRUE;
    }

    public Vector2f getMousePos() {
        double[] x = new double[1];
        double[] y = new double[1];
        glfwGetCursorPos(window, x, y);
        return new Vector2f((float)x[0], height-(float)y[0]);
    }
}
