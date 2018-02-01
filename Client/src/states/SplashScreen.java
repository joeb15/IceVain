package states;

import engine.physics.World;
import engine.render.guis.Gui;
import engine.render.guis.GuiManager;
import engine.render.guis.components.ClickComponent;
import engine.render.guis.components.EnterHoverComponent;
import engine.render.guis.components.ExitHoverComponent;
import engine.render.textures.Texture;
import engine.utils.events.Event;
import engine.utils.states.State;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;

public class SplashScreen extends State {

    private GuiManager guiManager;
    private World world;

    public SplashScreen(GuiManager guiManager, World world) {
        this.guiManager = guiManager;
        this.world=world;
    }

    public void onCreate() {
        addGuis();
    }

    public void onEvent(Event e) {

    }

    public void onDestroy() {
        guiManager.clearGuis();
    }

    private void addGuis() {
        Texture muffin = new Texture("/resources/muffin.jpeg");
        Texture stall = new Texture("/resources/stallTexture.png");
        Gui testGui = new Gui(muffin, 10,10,300,300);

        testGui.addComponent(new ClickComponent(testGui.getPos(), testGui.getSize(), GLFW_MOUSE_BUTTON_1) {
            public void onClick(Vector2f pos) {
                stateManager.setState(new GameState(guiManager, world));
            }
        });

        testGui.addComponent(new EnterHoverComponent(testGui.getPos(), testGui.getSize()) {
            public void onEnterHover(Vector2f pos, Vector2f lastPos) {
                testGui.setTexture(stall);
            }
        });

        testGui.addComponent(new ExitHoverComponent(testGui.getPos(), testGui.getSize()) {
            public void onExitHover(Vector2f pos, Vector2f lastPos) {
                testGui.setTexture(muffin);
            }
        });
        guiManager.addGui(testGui);
    }
}
