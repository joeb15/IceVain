package states;

import engine.render.fonts.BitmapFont;
import engine.render.guis.GuiManager;
import engine.utils.Config;
import engine.utils.GlobalVars;
import engine.utils.events.EventHandler;
import engine.utils.states.State;
import engine.world.World;
import events.FpsTpsDisplayEvent;

import static engine.utils.Utils.round;

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
//        FrameBuffer frameBuffer = guiManager.getWorldFrameBuffer();

//        Gui gui = new Gui(new Texture(frameBuffer, GlobalVars.FRAMEBUFFER_TEXTURE), 0, 0, frameBuffer.getWidth(), frameBuffer.getHeight());
//        gui.flipVertical();
//        gui.setLayer(0);
//        guiManager.addGui(gui);
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
//        world.addEntity(new Entity(ModelLoader.getTexturedModel("Pine_stump_1"), new Vector3f(36, world.getHeight(36, 20)+2, 20), new Vector3f(), new Vector3f(1,1,1)));

//        Entity barrel = new Entity(ModelLoader.getTexturedModel("Barrel"), new Vector3f(30,world.getHeight(30,20),20), new Vector3f(), new Vector3f(1,1,1));
//        world.addEntity(barrel);
//        Timer.createTimer(()-> barrel.rotate(0,.01f,0), 1000/60f, -1);
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
