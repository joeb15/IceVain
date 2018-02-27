package engine.render.shaders;

import engine.utils.VFS;
import org.joml.Matrix4f;

public class FontShader extends Shader{

    Uniform viewMatrix, transformationMatrix, textureSample, textureTransform;

    /**
     * Loads the shaders for font
     */
    public FontShader(){
        super(VFS.getFile("/shaders/font.vert"), VFS.getFile("/shaders/font.frag"));
    }

    /**
     * Loads the transformation matrix to the shader
     *
     * @param matrix The transformation matrix to load to the shader
     */
    public void loadTransformationMatrix(Matrix4f matrix){
        transformationMatrix.loadMatrix(matrix);
    }

    /**
     * Loads the view matrix to the shader
     *
     * @param matrix The view matrix to be loaded to the shader
     */
    public void loadViewMatrix(Matrix4f matrix){
        viewMatrix.loadMatrix(matrix);
    }

    /**
     * Loads the character transformation matrix to the shader
     *
     * @param matrix4f The character transformation matrix to be loaded to the shader
     */
    public void loadTextureTransform(Matrix4f matrix4f){
        textureTransform.loadMatrix(matrix4f);
    }

    /**
     * Loads all uniform locations from the shader
     */
    public void getUniformLocations() {
        textureSample = getUniform("textureSample");
        viewMatrix = getUniform("viewMatrix");
        transformationMatrix = getUniform("transformationMatrix");
        textureTransform = getUniform("textureTransform");
    }

    /**
     * Binds all attributes
     */
    public void bindAttributes() {
        bindAttribute(0, "position");
        bindAttribute(1, "textureCoords");
        bindAttribute(2, "normal");
    }

    /**
     * Loads the current texture page to the shader
     *
     * @param page The page to load
     */
    public void loadTexturePage(int page) {
        textureSample.load(page);
    }
}
