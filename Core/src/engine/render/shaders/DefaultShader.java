package engine.render.shaders;

import engine.entities.Light;
import engine.utils.Camera;
import engine.utils.VFS;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;

public class DefaultShader extends Shader{

    private static final int MAX_LIGHTS = 20;

    Uniform viewMatrix, projectionMatrix, textureSample, transformationMatrix, ambient;
    UniformStruct[] lights;

    public DefaultShader(){
        super(VFS.getFile("/shaders/default.vert"), VFS.getFile("/shaders/default.frag"));
    }

    public void loadTransformationMatrix(Matrix4f transformationMatrix) {
        this.transformationMatrix.loadMatrix(transformationMatrix);
    }

    public void loadProjectionMatrix(Camera camera){
        this.projectionMatrix.loadMatrix(camera.getProjection());
    }

    public void loadViewMatrix(Camera camera){
        this.viewMatrix.loadMatrix(camera.getViewMatrix());
    }

    public void loadAmbient(Vector3f color){
        ambient.loadVector(color);
    }

    public void loadLights(ArrayList<Light> lights){
        for(int i=0;i<lights.size()&&i<20;i++){
            //POSITION
            this.lights[i].getUniform(0).loadVector(lights.get(i).getPosition());
            //COLOR
            this.lights[i].getUniform(1).loadVector(lights.get(i).getColor());
            //ATTENUATION
            this.lights[i].getUniform(2).loadVector(lights.get(i).getAttenuation());
        }
        if(lights.size()!=20)
            this.lights[lights.size()].getUniform(2).loadVector(new Vector3f(0,0,0));
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
        ambient = getUniform("ambient");
        lights = new UniformStruct[MAX_LIGHTS];
        for(int i=0;i<MAX_LIGHTS;i++){
            lights[i] = getUniform("lights["+i+"]", "position","color", "attenuation");
        }
    }

    @Override
    public void bindAttributes() {
        bindAttribute(0, "position");
        bindAttribute(1, "textureCoords");
        bindAttribute(2, "normal");
    }
}
