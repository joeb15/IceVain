package engine.render.shaders;

import engine.utils.Camera;
import engine.utils.VFS;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class IDShader extends Shader{

    Uniform viewMatrix, projectionMatrix, transformationMatrix, color;

    /**
     * Loads the default shader files
     */
    public IDShader(){
        super(VFS.getFile("/shaders/id.vert"), VFS.getFile("/shaders/id.frag"));
    }

    /**
     * Loads the transformation matrix to the shader
     *
     * @param transformationMatrix The transformation matrix to be loaded to the shader
     */
    public void loadTransformationMatrix(Matrix4f transformationMatrix) {
        this.transformationMatrix.loadMatrix(transformationMatrix);
    }

    /**
     * Loads the entity ID color
     *
     * @param color The color of the entity
     */
    public void loadColor(Vector3f color){
        this.color.loadVector(color);
    }

    /**
     * Loads the projection matrix to the shader
     *
     * @param camera The camera to get the projection from
     */
    public void loadProjectionMatrix(Camera camera){
        this.projectionMatrix.loadMatrix(camera.getProjection());
    }

    /**
     * Loads the view matrix to the shader
     *
     * @param camera The camera to get the view matrix from
     */
    public void loadViewMatrix(Camera camera){
        this.viewMatrix.loadMatrix(camera.getViewMatrix());
    }

    /**
     * Loads all uniform locations when loading the shader
     */
    public void getUniformLocations() {
        color = getUniform("color");
        viewMatrix = getUniform("viewMatrix");
        projectionMatrix = getUniform("projectionMatrix");
        transformationMatrix = getUniform("transformationMatrix");
    }

    /**
     * Binds all of the attributes for the current shader
     */
    public void bindAttributes() {
        getUniformLocations();
        bindAttribute(0, "position");
    }

}
