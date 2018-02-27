package engine.render.shaders;

import engine.entities.Light;
import engine.utils.Camera;
import engine.utils.VFS;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;

public class DefaultShader extends Shader{

    private static final int MAX_LIGHTS = 10;

    Uniform viewMatrix, projectionMatrix, textureSample, transformationMatrix, ambient, shineDamper, reflectivity;
    UniformStruct[] lights;

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
     * Loads an array of lights to the shader
     *
     * @param lights The <code>ArrayList</code> that contains the lights
     */
    public void loadLights(ArrayList<Light> lights){
        for(int i=0;i<lights.size()&&i<MAX_LIGHTS;i++){
            //POSITION
            this.lights[i].getUniform(0).loadVector(lights.get(i).getPosition());
            //COLOR
            this.lights[i].getUniform(1).loadVector(lights.get(i).getColor());
            //ATTENUATION
            this.lights[i].getUniform(2).loadVector(lights.get(i).getAttenuation());
        }
        if(lights.size()<MAX_LIGHTS)
            this.lights[lights.size()].getUniform(2).loadVector(new Vector3f(0,0,0));
    }

    /**
     * Loads the texture to the shader
     */
    public void loadTexture(){
        textureSample.load(0);
    }

    /**
     * Loads the shine attributes to the shader
     *
     * @param shineDamper The shine dampening factor associated with the model
     * @param reflectivity The reflectivity value for the model
     */
    public void loadShineAttribs(float shineDamper, float reflectivity){
        this.shineDamper.loadFloat(shineDamper);
        this.reflectivity.loadFloat(reflectivity);
    }

    /**
     * Loads all uniform locations when loading the shader
     */
    public void getUniformLocations() {
        textureSample = getUniform("textureSample");
        viewMatrix = getUniform("viewMatrix");
        projectionMatrix = getUniform("projectionMatrix");
        transformationMatrix = getUniform("transformationMatrix");
        ambient = getUniform("ambient");
        lights = new UniformStruct[MAX_LIGHTS];
        for(int i=0;i<MAX_LIGHTS;i++){
            lights[i] = getUniform("lights["+i+"]", "position","color", "attenuation");
        }
        shineDamper = getUniform("shineDamper");
        reflectivity = getUniform("reflectivity");
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
