package engine.render.textures;

import engine.utils.Config;
import engine.utils.Debug;
import engine.utils.GlobalVars;
import engine.utils.VFS;
import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL14.GL_TEXTURE_LOD_BIAS;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.opengles.GLES20.GL_TEXTURE0;

public class Texture {

    private static ArrayList<Integer> textures = new ArrayList<>();
    private static HashMap<String, Texture> textureHashMap = new HashMap<>();

    private int id, width, height;
    private String path;

    /**
     * Interfaces to an OpenGL texture
     *
     * @param path The path to the texture file
     */
    public Texture(String path){
        this(VFS.getFile(path));
        Debug.log("Loading "+path);
        this.path = path;
        textureHashMap.put(path, this);
    }

    /**
     * Interfaces to an OpenGL texture
     *
     * @param file The file of the texture
     */
    private Texture(File file) {
        BufferedImage bi = null;
        try {
            bi = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert bi != null;
        width = bi.getWidth();
        height = bi.getHeight();

        int[] pixels = new int[width*height];
        bi.getRGB(0,0,width,height,pixels, 0, width);
        ByteBuffer byteBuffer = BufferUtils.createByteBuffer(height*width*4);
        for(int y=0;y<height;y++){
            for(int x=0;x<width;x++){
                int pixel = pixels[y*width+x];
                byteBuffer.put((byte)((pixel >> 16)&0xff));
                byteBuffer.put((byte)((pixel >>  8)&0xff));
                byteBuffer.put((byte)((pixel      )&0xff));
                byteBuffer.put((byte)((pixel >> 24)&0xff));
            }
        }
        byteBuffer.flip();
        id = glGenTextures();
        textures.add(id);
        glBindTexture(GL_TEXTURE_2D, id);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, byteBuffer);
        glGenerateMipmap(GL_TEXTURE_2D);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_LOD_BIAS, Config.getFloat(GlobalVars.CFG_LOD_BIAS));
    }

    /**
     * Frees all allocated memory space
     */
    public void cleanUp(){
        for(int i:textures)
            glDeleteTextures(i);
    }

    /**
     * Binds the texture
     */
    public void bind(int slot){
        glActiveTexture(GL_TEXTURE0+slot);
        glBindTexture(GL_TEXTURE_2D, id);
    }

    /**
     * Unbinds the texture
     */
    public void unbind(){
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    /**
     * Getter for a texture with a path
     *
     * @param texture The path of the texture
     * @return The texture located at that path
     */
    public static Texture getTexture(String texture) {
        if(!textureHashMap.containsKey(texture)) {
            return new Texture(texture);
        }
        return textureHashMap.get(texture);
    }

    /**
     * Gets the path of the texture
     *
     * @return The string path of the texture
     */
    public String getPath() {
        return path;
    }
}
