package engine.render.shaders;

import engine.utils.Camera;
import engine.utils.VFS;
import org.joml.Matrix4f;

public class DefaultShader extends Shader{

    Uniform viewMatrix, projectionMatrix, textureSample, transformationMatrix;

    public DefaultShader(){
        super(VFS.getFile("/shaders/default.vert"), VFS.getFile("/shaders/default.frag"));
    }

    public void loadTransformationMatrix(Matrix4f transformationMatrix) {
        this.transformationMatrix.loadMatrix(transformationMatrix);
    }

    public void loadViewMatrix(Camera camera){
        this.viewMatrix.loadMatrix(camera.getViewMatrix());
        this.projectionMatrix.loadMatrix(camera.getProjection());
    }

    public void loadTexture(){
        textureSample.load(0);
    }

    @Override
    public void getUniformLocations() {
        textureSample = getUniform("textureSample");
        viewMatrix = getUniform("viewMatrix");
        projectionMatrix = getUniform("projectionMatrix");
        transformationMatrix = getUniform("transformationMatrix");
    }

    @Override
    public void bindAttributes() {
        bindAttribute(0, "position");
        bindAttribute(1, "textureCoords");
    }
}
