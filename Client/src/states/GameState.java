package states;

import engine.entities.Entity;
import engine.entities.Light;
import engine.render.fonts.BitmapFont;
import engine.render.guis.GuiManager;
import engine.render.models.OBJLoader;
import engine.utils.Config;
import engine.utils.GlobalVars;
import engine.utils.Timer;
import engine.utils.events.EventHandler;
import engine.utils.states.State;
import engine.world.World;
import events.FpsTpsDisplayEvent;
import org.joml.Vector3f;

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
        addEntities();
        addGuis();
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
        world.addLight(new Light(new Vector3f(36, world.getHeight(36, 20), 20), new Vector3f(1,1,1), new Vector3f(1,0,-0.00001f)));
        world.addEntity(new Entity(OBJLoader.getTexturedModel("Pine_stump_1"), new Vector3f(36, world.getHeight(36, 20), 20), new Vector3f(), new Vector3f(1,1,1)));
        String[] models = {
            "_1","_2","_3","_snow_1","_snow_2","_snow_3","_stump_1","_stump_2"
        };

        for(int i=0;i<models.length;i++){
            Entity tree1 = new Entity(OBJLoader.getTexturedModel("Pine"+models[i]), new Vector3f(i*8+8, world.getHeight(i*8+8, 12), 12), new Vector3f(), new Vector3f(1,1,1));
            Entity tree2 = new Entity(OBJLoader.getTexturedModel("Evergreen"+models[i]), new Vector3f(i*8+8, world.getHeight(i*8+8, 20), 20), new Vector3f(), new Vector3f(1,1,1));
            Entity tree3 = new Entity(OBJLoader.getTexturedModel("Rainbow"+models[i]), new Vector3f(i*8+8, world.getHeight(i*8+8, 28), 28), new Vector3f(), new Vector3f(1,1,1));
            Timer.createTimer(()->{
                tree1.rotate(0,.01f,0);
                tree2.rotate(0,.01f,0);
                tree3.rotate(0,.01f,0);
            }, 1000/60f, -1);
            world.addEntity(tree1);
            world.addEntity(tree2);
            world.addEntity(tree3);
        }
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
