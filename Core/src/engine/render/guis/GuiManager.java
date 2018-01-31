package engine.render.guis;

import engine.render.guis.components.ClickComponent;
import engine.render.guis.components.GuiComponent;
import engine.render.textures.Texture;
import engine.utils.peripherals.Mouse;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.HashMap;

public class GuiManager {

    private HashMap<Texture, ArrayList<Gui>> guis;

    private boolean[] justClicked = new boolean[8];
    private float mouseX=0, mouseY=0;

    public GuiManager(){
        guis = new HashMap<>();
        Mouse.addMouseCallback((boolean[] pressed, boolean[] wasPressed, Vector2f lastPos, Vector2f pos, Vector2f[] lastDown)->{
            mouseX = pos.x;
            mouseY = pos.y;
            for(int i=0;i<8;i++){
                justClicked[i] = pressed[i] && !wasPressed[i];
            }
        });
    }

    public void addGui(Gui gui){
        Texture texture = gui.getTexture();
        if(!guis.containsKey(texture)){
            guis.put(texture, new ArrayList<>());
        }
        guis.get(texture).add(gui);
    }

    public HashMap<Texture, ArrayList<Gui>> getGuiHash() {
        return guis;
    }

    public void handleComponents(){
        for(ArrayList<Gui> guis:this.guis.values()){
            for(Gui gui:guis){
                for(GuiComponent gc:gui.getComponents()){
                    if(gc instanceof ClickComponent){
                        ((ClickComponent) gc).onClick(justClicked, mouseX, mouseY);
                    }
                }
            }
        }
    }
}
