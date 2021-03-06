package engine.render.framebuffers;

import engine.utils.Config;
import engine.utils.GlobalVars;
import engine.utils.events.EventHandler;

import java.nio.ByteBuffer;

import static engine.utils.GlobalVars.CFG_FRAME_HEIGHT;
import static engine.utils.GlobalVars.CFG_FRAME_WIDTH;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.GL_DEPTH_COMPONENT32;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.glFramebufferTexture;

public class FrameBuffer {

    private int w, h, fbo, texture, depth;

    /**
     * Creates a framebuffer instance
     *
     * @param w the width of the fbo
     * @param h the height of the fbo
     */
    public FrameBuffer(int w, int h){
        this.w=w;
        this.h=h;
        EventHandler.addEventCallback("cleanUp",(e)->cleanUp());
        createFBO();
        createTexture();
        createDepthTexture();
        unbind();
    }

    /**
     * Creates the framebuffer
     */
    private void createFBO(){
        fbo = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, fbo);
        glDrawBuffer(GL_COLOR_ATTACHMENT0);
    }

    /**
     * Creates the texture  attachment
     */
    private void createTexture(){
        texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, w, h, 0, GL_RGB, GL_UNSIGNED_BYTE, (ByteBuffer) null);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, texture, 0);
    }

    /**
     * Creates the depth texture attachment
     */
    private void createDepthTexture(){
        depth = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, depth);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT32, w, h, 0, GL_DEPTH_COMPONENT, GL_FLOAT, (ByteBuffer) null);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glFramebufferTexture(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, depth, 0);
    }

    /**
     * Binds the framebuffer
     */
    public void bind(){
        glBindFramebuffer(GL_FRAMEBUFFER, fbo);
        glViewport(0,0, w, h);
    }

    /**
     * Unbinds the framebuffer
     */
    public void unbind(){
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glViewport(0,0, Config.getInt(CFG_FRAME_WIDTH), Config.getInt(GlobalVars.CFG_FRAME_HEIGHT));
    }

    /**
     * Gets the texture of the fbo
     *
     * @return The texture of the fbo
     */
    public int getTexture(){
        return texture;
    }

    /**
     * Gets the depth buffer of the fbo
     *
     * @return Gets the depth buffer of the fbo
     */
    public int getDepthTexture(){
        return depth;
    }

    /**
     * Cleans up allocated memory
     */
    private void cleanUp(){
        glDeleteFramebuffers(fbo);
        glDeleteTextures(texture);
        glDeleteRenderbuffers(depth);
    }

    /**
     * Getter for the width of the framebuffer
     *
     * @return The width of the framebuffer
     */
    public int getWidth() {
        return w;
    }

    /**
     * Getter for the height of the framebuffer
     *
     * @return The height of the framebuffer
     */
    public int getHeight() {
        return h;
    }

    /**
     * Clears all depth and color buffer bits
     */
    public void clearBits() {
        glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
    }

    /**
     * Reads the pixel color at a certain position
     *
     * @param x The x coordinate to sample
     * @param y The y coordinate to sample
     *
     * @return The color's rgb at that position
     */
    public float[] getColorAt(int x, int y) {
        x*=w;
        x/=Config.getInt(CFG_FRAME_WIDTH);
        y*=h;
        y/=Config.getInt(CFG_FRAME_HEIGHT);
        float[] pixels = new float[3];
        bind();
        glReadPixels(x, y,1,1, GL_RGB, GL_FLOAT, pixels);
        unbind();
        return pixels;
    }

    /**
     * Reads the depth at a certain position
     *
     * @param x The x coordinate to sample
     * @param y The y coordinate to sample
     *
     * @return The color's rgb at that position
     */
    public float[] getDepthColorAt(int x, int y) {
        float[] pixels = new float[1];
        bind();
        glReadPixels(x, y,1,1, GL_DEPTH_COMPONENT, GL_FLOAT, pixels);
        unbind();
        return pixels;
    }
}
