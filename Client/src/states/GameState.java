package states;

import engine.entities.Entity;
import engine.entities.Light;
import engine.render.fonts.BitmapFont;
import engine.render.framebuffers.FrameBuffer;
import engine.render.guis.Gui;
import engine.render.guis.GuiManager;
import engine.render.guis.components.ClickComponent;
import engine.render.models.ModelLoader;
import engine.render.textures.Texture;
import engine.utils.Config;
import engine.utils.GlobalVars;
import engine.utils.Timer;
import engine.utils.events.EventHandler;
import engine.utils.states.State;
import engine.world.World;
import events.FpsTpsDisplayEvent;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static engine.utils.Utils.round;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;

public class GameState extends State {

    private GuiManager guiManager;
    private World world;
    private int lastFpsStringID = -1;
    private BitmapFont font;

    /**
     * The state that will display the currently running game function
     *
     * @param guiManager the general guiManager that handles all of the gui manageent code within the game
     * @param world the world instance that will be rendered from the game engine
     */
    public GameState(GuiManager guiManager, World world) {
        this.guiManager = guiManager;
        this.world = world;
        font = new BitmapFont("/fonts/Arial.fnt");
    }

    /**
     * A method to run on the creation/switching to a game state
     */
    public void onCreate() {
        addGuis();
        FrameBuffer frameBuffer = guiManager.getWorldFrameBuffer();
        FrameBuffer idFrameBuffer = guiManager.getIDFrameBuffer();
        Gui gui = new Gui(new Texture(frameBuffer, GlobalVars.FRAMEBUFFER_TEXTURE), 0, 0, frameBuffer.getWidth(), frameBuffer.getHeight());
        gui.flipVertical();
        gui.setLayer(0);
        gui.addComponent(new ClickComponent(gui.getPos(), gui.getSize(), GLFW_MOUSE_BUTTON_1) {
            public void onClick(Vector2f pos) {
                float[] pixels = idFrameBuffer.getColorAt((int)pos.x,(int)pos.y);
                int id = Math.round(255*pixels[0]) + 256*Math.round(255*pixels[1]) + 256*256*Math.round(255*pixels[2]);
                Entity e = world.getEntity(id);
                if(e!=null) {
                    long startTime = System.currentTimeMillis();
                    Timer.createTimer(()->{
                        long currTime = System.currentTimeMillis();
                        float time = (currTime-startTime)/1000f;
                        float scl = 1+(float)Math.sin(time*Math.PI);
                        float scl2 = 2-(float)Math.abs(Math.cos(time*Math.PI));
                        e.scale(scl2,scl,scl2);
                    }, 2, 500);
                }
            }
        });
        guiManager.addGui(gui);
        addEntities();
    }

    /**
     * A method to run on the destruction of a game state
     */
    public void onDestroy() {
        guiManager.clearGuis();
        world.clearEntities();
    }

    /**
     * Adds random entities to the world for testing
     */
    private void addEntities(){
        world.addLight(new Light(new Vector3f(36, world.getHeight(36, 20)+2, 20), new Vector3f(1,1,1), new Vector3f(1,0,-0.00001f)));
        world.addEntity(new Entity(ModelLoader.getTexturedModel("Pine_stump_1"), new Vector3f(36, world.getHeight(36, 20)+2, 20), new Vector3f(), new Vector3f(1,1,1)));
        String[] models = {
            "_1","_2","_3","_snow_1","_snow_2","_snow_3","_stump_1","_stump_2"
        };

        for(int j=0;j<10;j++) {
            for (int i = 0; i < models.length; i++) {
                Entity tree1 = new Entity(ModelLoader.getTexturedModel("Pine" + models[i]), new Vector3f(i + j*8 + 8, world.getHeight(i + j*8 + 8, 12), 12), new Vector3f(), new Vector3f(1, 1, 1));
                Entity tree2 = new Entity(ModelLoader.getTexturedModel("Evergreen" + models[i]), new Vector3f(i + j*8 + 8, world.getHeight(i + j*8 + 8, 20), 20), new Vector3f(), new Vector3f(1, 1, 1));
                Entity tree3 = new Entity(ModelLoader.getTexturedModel("Rainbow" + models[i]), new Vector3f(i + j*8 + 8, world.getHeight(i + j*8 + 8, 28), 28), new Vector3f(), new Vector3f(1, 1, 1));
                world.addEntity(tree1);
                world.addEntity(tree2);
                world.addEntity(tree3);
            }
        }

        Entity barrel = new Entity(ModelLoader.getTexturedModel("Barrel"), new Vector3f(30,world.getHeight(30,20),20), new Vector3f(), new Vector3f(1,1,1));
        world.addEntity(barrel);
        Entity stump = new Entity(ModelLoader.getTexturedModel("Rainbow_stump_2"), new Vector3f(20.5f, world.getHeight(20.5f, 20.5f), 20.5f), new Vector3f(), new Vector3f(1,1,1));
        world.addEntity(stump);
        Timer.createTimer(()-> barrel.rotate(0,.01f,0), 1000/60f, -1);
    }

    /**
     * Helper method to add all of the guis/string displays needed in the gamestate
     */
    private void addGuis(){
        EventHandler.addEventCallback("fpsTpsDisplay", (evt)->{
            FpsTpsDisplayEvent event = (FpsTpsDisplayEvent)evt;
            guiManager.removeString(lastFpsStringID);
            lastFpsStringID = guiManager.addString(font, "FPS:"+round(event.fps, 1)+"\nTPS:"+round(event.tps), 0, Config.getInt(GlobalVars.CFG_FRAME_HEIGHT)-font.getCharHeight(20), 20);
        });
    }
}
