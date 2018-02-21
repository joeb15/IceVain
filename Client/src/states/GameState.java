package states;

import engine.entities.Entity;
import engine.entities.Light;
import engine.physics.World;
import engine.render.fonts.BitmapFont;
import engine.render.guis.GuiManager;
import engine.render.models.OBJLoader;
import engine.render.models.RawModel;
import engine.render.models.TexturedModel;
import engine.render.textures.ModelTexture;
import engine.render.textures.Texture;
import engine.utils.Config;
import engine.utils.GlobalVars;
import engine.utils.Timer;
import engine.utils.events.EventHandler;
import engine.utils.states.State;
import events.FpsTpsDisplayEvent;
import org.joml.Vector3f;

public class GameState extends State {

    private GuiManager guiManager;
    private World world;
    private int lastFpsStringID = -1;
    private BitmapFont font;

    public GameState(GuiManager guiManager, World world) {
        this.guiManager = guiManager;
        this.world = world;
        font = new BitmapFont("/fonts/Arial.fnt");
    }

    public void onCreate() {
        addEntities();
        addGuis();
    }

    public void onDestroy() {
        guiManager.clearGuis();
        world.clearEntities();
    }

    /**
     * Adds random entities to the world for testing
     */
    private void addEntities(){
        ModelTexture modelTexture2 = new ModelTexture(new Texture("/resources/stallTexture.png"));
        RawModel model2 = OBJLoader.loadModel("/resources/stall.obj");
        TexturedModel texturedModel2 = new TexturedModel(model2, modelTexture2);

        Entity stall = new Entity(texturedModel2, new Vector3f(), new Vector3f(), new Vector3f(1,1,1));
        Entity stall2 = new Entity(texturedModel2, new Vector3f(), new Vector3f(), new Vector3f(1,1,1));

        world.addEntity(stall);
        world.addEntity(stall2);
        Timer.createTimer(()->{
            stall.setRotation(0, -Timer.getTime(), 0);
            stall.setPos(-6, (float)(-2.5+Math.sin(Timer.getTime()*1.25)), -20);
            stall2.setRotation(0, Timer.getTime(), 0);
            stall2.setPos(6, (float)(-2.5-Math.sin(Timer.getTime()*1.25)), -20);
        }, 1000/60f, -1);
        world.addLight(new Light(new Vector3f(0, 0, 0), new Vector3f(1,1,1), new Vector3f(1,-.1f,0)));
    }

    private void addGuis(){
        EventHandler.addEventCallback("fpsTpsDisplay", (evt)->{
            FpsTpsDisplayEvent event = (FpsTpsDisplayEvent)evt;
            guiManager.removeString(lastFpsStringID);
            lastFpsStringID = guiManager.addString(font, "FPS:"+event.fps+"\nTPS:"+event.tps, 0, Config.getInt(GlobalVars.CFG_FRAME_HEIGHT)-font.getCharHeight(20), 20);
        });
    }
}
