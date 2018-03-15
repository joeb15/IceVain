package engine.render.shaders;

import engine.utils.Camera;
import engine.utils.VFS;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class DefaultShader extends Shader{

    private static final int MAX_MATERIALS = 8;

    Uniform viewMatrix, projectionMatrix, transformationMatrix, ambient;
    Uniform[] textureSample;

    /**
     * Loads the default shader files
     */
    public DefaultShader(){
        super(VFS.getFile("/shaders/default.vert"), VFS.getFile("/shaders/default.frag"));
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
     * Loads the ambient color to the shader
     *
     * @param color The ambient color to be used in rendering
     */
    public void loadAmbient(Vector3f color){
        ambient.loadVector(color);
    }

    /**
     * Loads the texture to the shader
     */
    public void loadTexture(){
        for(int i=0;i<MAX_MATERIALS;i++) {
            textureSample[i].load(i);
        }
    }

    /**
     * Loads all uniform locations when loading the shader
     */
    public void getUniformLocations() {
        viewMatrix = getUniform("viewMatrix");
        projectionMatrix = getUniform("projectionMatrix");
        transformationMatrix = getUniform("transformationMatrix");
        ambient = getUniform("ambient");
        textureSample = new Uniform[MAX_MATERIALS];
        for(int i=0;i<MAX_MATERIALS;i++) {
            textureSample[i] = getUniform("textureSample["+i+"]");
        }
    }

    /**
     * Binds all of the attributes for the current shader
     */
    public void bindAttributes() {
        getUniformLocations();
        bindAttribute(0, "position");
        bindAttribute(1, "textureCoords");
        bindAttribute(2, "normal");
    }
}
