package engine.render.shaders;

import engine.utils.VFS;
import org.joml.Matrix4f;

public class GuiShader extends Shader{

    Uniform viewMatrix, transformationMatrix, textureSample;

    /**
     * Loads the shaders for the gui renderer
     */
    public GuiShader(){
        super(VFS.getFile("/shaders/gui.vert"), VFS.getFile("/shaders/gui.frag"));
    }

    /**
     * Loads the texture to the shader
     */
    public void loadTexture(){
        textureSample.load(0);
    }

    /**
     * Loads the transformation matrix to the shader
     *
     * @param matrix The matrix to be loaded to the shader
     */
    public void loadTransformationMatrix(Matrix4f matrix){
        transformationMatrix.loadMatrix(matrix);
    }

    /**
     * Loads the view matrix to the shader
     *
     * @param matrix The matrix to load to the shader
     */
    public void loadViewMatrix(Matrix4f matrix){
        viewMatrix.loadMatrix(matrix);
    }

    /**
     * Loads all uniform locations when the shader is loaded
     */
    public void getUniformLocations() {
        textureSample = getUniform("textureSample");
        viewMatrix = getUniform("viewMatrix");
        transformationMatrix = getUniform("transformationMatrix");
    }

    /**
     * Binds all used attributes to thier respective values
     */
    public void bindAttributes() {
        bindAttribute(0, "position");
        bindAttribute(1, "textureCoords");
        bindAttribute(2, "normal");
    }
}
