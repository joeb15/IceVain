package engine.render.renderers;

import engine.render.guis.Gui;
import engine.render.guis.GuiManager;
import engine.render.models.RawModel;
import engine.render.shaders.GuiShader;
import engine.render.textures.Texture;
import engine.utils.Config;
import engine.utils.GlobalVars;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class GuiRenderer{

    private GuiShader shader;
    private GuiManager guiManager;
    private Matrix4f viewMatrix;

    /**
     * Renderer for all of the guis
     *
     * @param guiManager The guiManager to handle all of the guis
     */
    public GuiRenderer(GuiManager guiManager){
        shader = new GuiShader();
        this.guiManager=guiManager;
        int w = Config.getInt(GlobalVars.CFG_FRAME_WIDTH);
        int h = Config.getInt(GlobalVars.CFG_FRAME_HEIGHT);
        viewMatrix = new Matrix4f().ortho2D(0,w,0,h);
    }

    /**
     * Renders all of the guis to the screen
     */
    public void render(){
        shader.bind();
        RawModel rawModel = Gui.getRect();
        glBindVertexArray(rawModel.getVaoId());
        shader.loadViewMatrix(viewMatrix);
        shader.loadTexture();
        HashMap<Texture, ArrayList<Gui>> guiHash = guiManager.getGuiHash();
        for(Texture texture : guiHash.keySet()){
            for(int i:shader.attribs)
                glEnableVertexAttribArray(i);
            texture.bind(0);
            for(Gui gui:guiHash.get(texture)) {
                shader.loadTransformationMatrix(gui.getTransformationMatrix());
                glDrawElements(GL_TRIANGLES, rawModel.getVertexCount(), GL_UNSIGNED_INT, 0);
            }
            for(int i:shader.attribs)
                glDisableVertexAttribArray(i);
        }
        glBindVertexArray(0);
        shader.unbind();
    }

    /**
     * Frees up all allocated memory
     */
    public void cleanUp(){
        shader.cleanUp();
    }

}
