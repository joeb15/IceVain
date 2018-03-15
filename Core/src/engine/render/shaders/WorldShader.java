package engine.render.shaders;

import engine.utils.Camera;
import engine.utils.VFS;
import org.joml.Vector3f;

public class WorldShader extends Shader{

    Uniform viewMatrix, projectionMatrix, ambient, textureSample, pos;

    /**
     * Loads the default shader files
     */
    public WorldShader(){
        super(VFS.getFile("/shaders/world.vert"), VFS.getFile("/shaders/world.frag"));
    }

    /**
     * Loads the block position to the shader
     *
     * @param x The block's x pos
     * @param y The block's y pos
     * @param z The block's z pos
     */
    public void loadPosition(int x, int y, int z) {
        pos.loadVector(new Vector3f(x, y, z));
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
        textureSample.load(0);
    }

    /**
     * Loads all uniform locations when loading the shader
     */
    public void getUniformLocations() {
        viewMatrix = getUniform("viewMatrix");
        projectionMatrix = getUniform("projectionMatrix");
        pos = getUniform("translation");
        ambient = getUniform("ambient");
        textureSample = getUniform("textureSample");
    }

    /**
     * Binds all of the attributes for the current shader
     */
    public void bindAttributes() {
        getUniformLocations();
        bindAttribute(0, "position");
        bindAttribute(1, "textureCoords");
    }
}
