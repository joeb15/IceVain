package engine.render.guis;

import engine.render.fonts.BitmapFont;
import engine.render.fonts.CharacterWithPos;
import engine.render.guis.components.*;
import engine.render.textures.Texture;
import engine.utils.Utils;
import engine.utils.peripherals.Mouse;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class GuiManager {

    private HashMap<Texture, ArrayList<Gui>> guiHash;
    private HashMap<BitmapFont, CopyOnWriteArrayList<CharacterWithPos>> fontHash = new HashMap<>();
    private CopyOnWriteArrayList<Gui> guis;
    private boolean[] pressed = new boolean[8];
    private boolean[] wasPressed = new boolean[8];
    private Vector2f pos = new Vector2f();
    private Vector2f lastPos = new Vector2f();
    private Vector2f[] lastDown;
    private int stringID = 0 ;

    /**
     * A managing class to handle all of the guis and components being used in the game
     */
    public GuiManager(){
        guis = new CopyOnWriteArrayList<>();
        guiHash = new HashMap<>();
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

    /**
     * Adds a gui to the render hash
     *
     * @param gui The gui to be added
     */
    public void addGui(Gui gui){
        guis.add(gui);
    }

    /**
     * Getter for the gui hash that will be rendered
     *
     * @return The <code>HashMap</code> associated with the gui rendering hash
     */
    public HashMap<Texture, ArrayList<Gui>> getGuiHash() {
        guiHash.clear();
        for(Gui gui:guis){
            Texture texture = gui.getTexture();
            if(!guiHash.containsKey(texture)){
                guiHash.put(texture, new ArrayList<>());
            }
            guiHash.get(texture).add(gui);
        }
        return guiHash;
    }

    /**
     * Handles all of the components of guis and manages their callback methods
     */
    public void handleComponents(){
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
                }else if(gc instanceof MousePressedComponent){
                    MousePressedComponent mousePressedComponent = (MousePressedComponent) gc;
                    if(pressed[mousePressedComponent.getMouseButton()] && Utils.contains(mousePressedComponent, pos)){
                        mousePressedComponent.onPressed(pos);
                    }
                }else if(gc instanceof MouseReleasedComponent){
                    MouseReleasedComponent mouseReleasedComponent = (MouseReleasedComponent) gc;
                    if(!pressed[mouseReleasedComponent.getMouseButton()] && Utils.contains(mouseReleasedComponent, pos)){
                        mouseReleasedComponent.onReleased(pos);
                    }
                }else if(gc instanceof MouseJustPressedComponent){
                    MouseJustPressedComponent mousePressedComponent = (MouseJustPressedComponent) gc;
                    if(pressed[mousePressedComponent.getMouseButton()] && !wasPressed[mousePressedComponent.getMouseButton()] && Utils.contains(mousePressedComponent, pos)){
                        mousePressedComponent.onPressed(pos);
                    }
                }else if(gc instanceof MouseJustReleasedComponent){
                    MouseJustReleasedComponent mouseReleasedComponent = (MouseJustReleasedComponent) gc;
                    if(!pressed[mouseReleasedComponent.getMouseButton()] && wasPressed[mouseReleasedComponent.getMouseButton()] && Utils.contains(mouseReleasedComponent, pos)){
                        mouseReleasedComponent.onReleased(pos);
                    }
                }
            }
        }
    }

    /**
     * Clears all the guis and strings from the render hash
     */
    public void clearGuis() {
        guis.clear();
        fontHash.clear();
    }

    /**
     * Removes a string given the ID that was given to it upon creation
     *
     * @param stringID The ID that is associated with the string
     */
    public void removeString(int stringID){
        for(CopyOnWriteArrayList<CharacterWithPos> al:fontHash.values()){
            for(CharacterWithPos c:al){
                if(c.id==stringID)
                    al.remove(c);
            }
        }
    }

    /**
     * Adds a string to the render hash
     *
     * @param font The font to use
     * @param text The text to render
     * @param x The x position of the lower-left corner of the string
     * @param y The y position of the lower-left corner of the string
     * @param fontSize The size of the font to be used
     * @return The stringID that will be used to modify this string
     */
    public int addString(BitmapFont font, String text, float x, float y, float fontSize){
        float pointer = x;
        float ratio = fontSize/font.getFontSize();
        float yoff=0;
        if(!fontHash.containsKey(font))
            fontHash.put(font, new CopyOnWriteArrayList<>());
        for(int c:text.chars().toArray()){
            if(c=='\n'){
                pointer=x;
                yoff+=font.getCharHeight()*ratio;
                continue;
            }
            if(font.getChar(c)==null)
                continue;
            CharacterWithPos characterWithPos = new CharacterWithPos();
            pointer += font.getChar(c).xoff*ratio;
            characterWithPos.fontSize=fontSize;
            characterWithPos.x=pointer;
            characterWithPos.y=y-yoff+(font.getCharHeight()-font.getChar(c).charH-font.getChar(c).yoff)*ratio;
            characterWithPos.character=c;
            characterWithPos.id=stringID;
            fontHash.get(font).add(characterWithPos);
            pointer += font.getChar(c).x*ratio;
            pointer += font.getChar(c).xadv*ratio;
        }
        return stringID++;
    }

    /**
     * Getter for the text rendering font hash
     *
     * @return The <code>HashMap</code> associated with the font rendering hash
     */
    public HashMap<BitmapFont, CopyOnWriteArrayList<CharacterWithPos>> getFontHash() {
        return fontHash;
    }
}
