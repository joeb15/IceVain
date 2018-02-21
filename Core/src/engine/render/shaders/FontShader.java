package engine.render.shaders;

import engine.utils.VFS;
import org.joml.Matrix4f;

public class FontShader extends Shader{

    Uniform viewMatrix, transformationMatrix, textureSample[], textureTransform, pageNum;

    public FontShader(){
        super(VFS.getFile("/shaders/font.vert"), VFS.getFile("/shaders/font.frag"));
    }

    public void loadTextures(){
        for(int i=0;i<10;i++)
            textureSample[i].load(i);
    }

    public void loadTransformationMatrix(Matrix4f matrix){
        transformationMatrix.loadMatrix(matrix);
    }

    public void loadViewMatrix(Matrix4f matrix){
        viewMatrix.loadMatrix(matrix);
    }

    public void loadTextureTransform(Matrix4f matrix4f){
        textureTransform.loadMatrix(matrix4f);
    }

    @Override
    public void getUniformLocations() {
        textureSample = new Uniform[10];
        for(int i=0;i<10;i++)
            textureSample[i] = getUniform("textureSample["+i+"]");
        viewMatrix = getUniform("viewMatrix");
        transformationMatrix = getUniform("transformationMatrix");
        textureTransform = getUniform("textureTransform");
        pageNum = getUniform("pageNum");
    }

    @Override
    public void bindAttributes() {
        bindAttribute(0, "position");
        bindAttribute(1, "textureCoords");
        bindAttribute(2, "normal");
    }

    public void loadTexturePage(int page) {
        pageNum.load(page);
    }
}
