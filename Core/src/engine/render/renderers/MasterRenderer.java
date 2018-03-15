package engine.render.renderers;

import engine.render.Window;
import engine.render.guis.GuiManager;
import engine.utils.Camera;
import engine.world.World;

import static org.lwjgl.opengl.GL11.*;

public class MasterRenderer {

    private Window window;
    private Renderer renderer;
    private GuiRenderer guiRenderer;
    private FontRenderer fontRenderer;
    private WorldRenderer worldRenderer;

    private GuiManager guiManager;
    private Camera camera;

    /**
     * The renderer to render all parts of the game
     *
     * @param window The window to render to
     * @param camera The camera that will be used for the view matrix
     * @param world The world to be rendered
     * @param guiManager The guiManager to handle all guis and strings
     */
    public MasterRenderer(Window window, Camera camera, World world, GuiManager guiManager){
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        this.window = window;
        this.guiManager = guiManager;
        this.camera = camera;
        worldRenderer = new WorldRenderer(world, camera);
        renderer = new Renderer(world, camera);
        guiRenderer = new GuiRenderer(guiManager);
        fontRenderer = new FontRenderer(guiManager);
    }

    /**
     * Renders the game
     */
    public void render(){
        glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);

        glEnable(GL_CULL_FACE);
        worldRenderer.render();
        renderer.render();

        glDisable(GL_CULL_FACE);
        guiRenderer.render();
        fontRenderer.render();

        window.swapBuffers();
    }

    /**
     * Frees all allocated memory on program close
     */
    public void cleanUp(){
        worldRenderer.cleanUp();
        renderer.cleanUp();
        guiRenderer.cleanUp();
        fontRenderer.cleanUp();
    }

}
