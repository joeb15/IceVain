package engine.render.renderers;

import engine.physics.World;
import engine.render.Window;
import engine.render.guis.GuiManager;
import engine.utils.Camera;

public class MasterRenderer {

    private Window window;
    private Renderer renderer;
    private GuiRenderer guiRenderer;
    private FontRenderer fontRenderer;

    public MasterRenderer(Window window, Camera camera, World world, GuiManager guiManager){
        this.window = window;
        renderer = new Renderer(world, camera);
        guiRenderer = new GuiRenderer(guiManager);
        fontRenderer = new FontRenderer(guiManager);
    }

    public void render(){
        renderer.render();
        guiRenderer.render();
        fontRenderer.render();
        window.swapBuffers();
    }

    public void cleanUp(){
        renderer.cleanUp();
        guiRenderer.cleanUp();
        fontRenderer.cleanUp();
    }

}
