package engine.render.renderers;

import engine.render.fonts.BitmapChar;
import engine.render.fonts.BitmapFont;
import engine.render.fonts.CharacterWithPos;
import engine.render.guis.Gui;
import engine.render.guis.GuiManager;
import engine.render.models.RawModel;
import engine.render.shaders.FontShader;
import engine.utils.Config;
import engine.utils.GlobalVars;
import org.joml.Matrix4f;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class FontRenderer {

    private FontShader shader;
    private GuiManager guiManager;
    private Matrix4f viewMatrix;

    /**
     * A renderer to render all of the fonts
     *
     * @param guiManager The guiManager that handles all of the strings
     */
    public FontRenderer(GuiManager guiManager){
        shader = new FontShader();
        this.guiManager=guiManager;
        int w = Config.getInt(GlobalVars.CFG_FRAME_WIDTH);
        int h = Config.getInt(GlobalVars.CFG_FRAME_HEIGHT);
        viewMatrix = new Matrix4f().ortho2D(0,w,0,h);
    }

    /**
     * Renders all of the strings to the screen
     */
    public void render(){
        shader.bind();
        RawModel rawModel = Gui.getRect();
        glBindVertexArray(rawModel.getVaoId());
        shader.loadViewMatrix(viewMatrix);
        HashMap<BitmapFont, CopyOnWriteArrayList<CharacterWithPos>> fontHash = guiManager.getFontHash();
        for(BitmapFont font : fontHash.keySet()){
            for(int i:shader.attribs)
                glEnableVertexAttribArray(i);
            for(int i=0;i<font.getPageImages().length;i++)
                font.getPageImages()[i].bind(i);
            for(CharacterWithPos characterWithPos: fontHash.get(font)) {
                BitmapChar bitmapChar = font.getChar(characterWithPos.character);
                shader.loadTexturePage(bitmapChar.page);
                shader.loadTextureTransform(new Matrix4f()
                        .translate(bitmapChar.x, bitmapChar.y,0)
                        .scale(bitmapChar.w, bitmapChar.h, 0));
                shader.loadTransformationMatrix(new Matrix4f()
                        .translate(characterWithPos.x, characterWithPos.y,0)
                        .scale(bitmapChar.charW*characterWithPos.fontSize/font.getFontSize(), bitmapChar.charH*characterWithPos.fontSize/font.getFontSize(), 0));
                glDrawElements(GL_TRIANGLES, rawModel.getVertexCount(), GL_UNSIGNED_INT, 0);
            }
            for(int i:shader.attribs)
                glDisableVertexAttribArray(i);
        }
        glBindVertexArray(0);
        shader.unbind();
    }

    /**
     * Cleans up all resources created by the renderer
     */
    public void cleanUp(){
        shader.cleanUp();
    }

}
