package engine.render.renderers;

import engine.physics.World;
import engine.render.Window;
import engine.utils.Camera;

public class MasterRenderer {

    private Window window;
    private Renderer renderer;

    public MasterRenderer(Window window, Camera camera, World world){
        this.window = window;
        renderer = new Renderer(world, camera);
    }

    public void render(){
        renderer.render();
        window.swapBuffers();
    }

    public void cleanUp(){
        renderer.cleanUp();
    }

}
