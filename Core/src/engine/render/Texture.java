package engine.render;

import engine.utils.VFS;
import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;

public class Texture {

    private int id, width, height;

    public Texture(String path){
        File file = VFS.getFile(path);
        BufferedImage bi;
        try {
            bi = ImageIO.read(file);
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
            glBindTexture(GL_TEXTURE_2D, id);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, byteBuffer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Binds the texture
     */
    public void bind(){
        glBindTexture(GL_TEXTURE_2D, id);
    }

    /**
     * Unbinds the texture
     */
    public void unbind(){
        glBindTexture(GL_TEXTURE_2D, 0);
    }
}
