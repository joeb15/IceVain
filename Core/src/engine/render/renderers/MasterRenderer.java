package engine.render.renderers;

import engine.render.Window;
import engine.render.guis.GuiManager;
import engine.utils.Camera;
import engine.world.World;

public class MasterRenderer {

    private Window window;
    private Renderer renderer;
    private GuiRenderer guiRenderer;
    private FontRenderer fontRenderer;

    /**
     * The renderer to render all parts of the game
     *
     * @param window The window to render to
     * @param camera The camera that will be used for the view matrix
     * @param world The world to be rendered
     * @param guiManager The guiManager to handle all guis and strings
     */
    public MasterRenderer(Window window, Camera camera, World world, GuiManager guiManager){
        this.window = window;
        renderer = new Renderer(world, camera);
        guiRenderer = new GuiRenderer(guiManager);
        fontRenderer = new FontRenderer(guiManager);
    }

    /**
     * Renders the game
     */
    public void render(){
        renderer.render();
        guiRenderer.render();
        fontRenderer.render();
        window.swapBuffers();
    }

    /**
     * Frees all allocated memory on program close
     */
    public void cleanUp(){
        renderer.cleanUp();
        guiRenderer.cleanUp();
        fontRenderer.cleanUp();
    }

}
