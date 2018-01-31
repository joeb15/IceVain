package engine.render.shaders;

import engine.utils.VFS;
import org.joml.Matrix4f;

public class GuiShader extends Shader{

    Uniform viewMatrix, transformationMatrix, textureSample;

    public GuiShader(){
        super(VFS.getFile("/shaders/gui.vert"), VFS.getFile("/shaders/gui.frag"));
    }

    public void loadTexture(){
        textureSample.load(0);
    }

    public void loadTransformationMatrix(Matrix4f matrix){
        transformationMatrix.loadMatrix(matrix);
    }

    public void loadViewMatrix(Matrix4f matrix){
        viewMatrix.loadMatrix(matrix);
    }

    @Override
    public void getUniformLocations() {
        textureSample = getUniform("textureSample");
        viewMatrix = getUniform("viewMatrix");
        transformationMatrix = getUniform("transformationMatrix");
    }

    @Override
    public void bindAttributes() {
        bindAttribute(0, "position");
        bindAttribute(1, "textureCoords");
        bindAttribute(2, "normal");
    }
}
