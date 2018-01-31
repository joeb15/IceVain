package engine.render.guis;

import engine.render.guis.components.*;
import engine.render.textures.Texture;
import engine.utils.Utils;
import engine.utils.peripherals.Mouse;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.HashMap;

public class GuiManager {

    private HashMap<Texture, ArrayList<Gui>> guis;

    private boolean[] pressed = new boolean[8];
    private boolean[] wasPressed = new boolean[8];
    private Vector2f pos = new Vector2f();
    private Vector2f lastPos = new Vector2f();
    private Vector2f[] lastDown;

    public GuiManager(){
        guis = new HashMap<>();
        Mouse.addMouseCallback((boolean[] pressed, boolean[] wasPressed, Vector2f lastPos, Vector2f pos, Vector2f[] lastDown)->{
            this.lastPos=this.pos;
            this.pos=pos;
            this.lastDown=lastDown;
            for(int i=0;i<8;i++){
                this.pressed[i] = pressed[i];
                this.wasPressed[i] = wasPressed[i];
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
                        ClickComponent clickComponent = (ClickComponent) gc;
                        if(!pressed[clickComponent.getMouseButton()] && wasPressed[clickComponent.getMouseButton()] &&
                                Utils.contains(clickComponent, pos) &&
                                Utils.contains(clickComponent, lastDown[clickComponent.getMouseButton()])){
                            clickComponent.onClick(pos);
                        }
                    }else if(gc instanceof HoverComponent){
                        HoverComponent hoverComponent = (HoverComponent) gc;
                        if(Utils.contains(hoverComponent, pos)){
                            hoverComponent.onHover(pos);
                        }
                    }else if(gc instanceof EnterHoverComponent){
                        EnterHoverComponent enterHoverComponent = (EnterHoverComponent) gc;
                        if(!Utils.contains(enterHoverComponent, lastPos) && Utils.contains(enterHoverComponent, pos)) {
                            enterHoverComponent.onEnterHover(pos, lastPos);
                        }
                    }else if(gc instanceof ExitHoverComponent){
                        ExitHoverComponent exitHoverComponent = (ExitHoverComponent) gc;
                        if(Utils.contains(exitHoverComponent, lastPos) && !Utils.contains(exitHoverComponent, pos)) {
                            exitHoverComponent.onExitHover(pos, lastPos);
                        }
                    }
                }
            }
        }
    }
}