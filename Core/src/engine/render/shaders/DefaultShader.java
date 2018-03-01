package engine.render.shaders;

import engine.entities.Light;
import engine.render.models.Material;
import engine.render.models.MaterialLibrary;
import engine.utils.Camera;
import engine.utils.VFS;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Collection;

public class DefaultShader extends Shader{

    private static final int MAX_LIGHTS = 10;
    private static final int MAX_MATERIALS = 8;

    Uniform viewMatrix, projectionMatrix, transformationMatrix, ambient;
    UniformStruct[] lights;
    Uniform[] specularExponent, reflectivity, ambientMult, diffuseMult, specularMult, textureSample, normalSample, specularSample;

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
        for(int i=0;i<MAX_MATERIALS;i++) {
            textureSample[i].load(i);
            normalSample[i].load(i+MAX_MATERIALS);
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
        lights = new UniformStruct[MAX_LIGHTS];
        for(int i=0;i<MAX_LIGHTS;i++){
            lights[i] = getUniform("lights["+i+"]", "position","color", "attenuation");
        }
        textureSample = new Uniform[MAX_MATERIALS];
        normalSample = new Uniform[MAX_MATERIALS];
        specularSample = new Uniform[MAX_MATERIALS];
        specularExponent = new Uniform[MAX_MATERIALS];
        reflectivity = new Uniform[MAX_MATERIALS];
        ambientMult = new Uniform[MAX_MATERIALS];
        diffuseMult = new Uniform[MAX_MATERIALS];
        specularMult = new Uniform[MAX_MATERIALS];
        for(int i=0;i<MAX_MATERIALS;i++) {
            textureSample[i] = getUniform("textureSample["+i+"]");
            normalSample[i] = getUniform("normalSample["+i+"]");
            specularSample[i] = getUniform("specularSample["+i+"]");
            specularExponent[i] = getUniform("specularExponent[" + i + "]");
            reflectivity[i] = getUniform("reflectivity[" + i + "]");
            ambientMult[i] = getUniform("ambientMult[" + i + "]");
            diffuseMult[i] = getUniform("diffuseMult[" + i + "]");
            specularMult[i] = getUniform("specularMult[" + i + "]");
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
        bindAttribute(3, "material");
        bindAttribute(4, "tangent");
    }

    /**
     * Loads a material library to the shader
     *
     * @param materialLibrary The material library to load
     */
    public void loadMaterialLibrary(MaterialLibrary materialLibrary) {
        assert materialLibrary!=null;

        materialLibrary.bindTextures();
        materialLibrary.bindNormals(MAX_MATERIALS);
        materialLibrary.bindSpecular(MAX_MATERIALS*2);
        Collection<Material> materials = materialLibrary.getMaterials();
        for(Material m:materials){
            loadMaterial(m.id, m);
        }
        if(materials.size()==0){
            loadMaterial(0, Material.getDefaultMaterial());
        }
    }

    /**
     * Helper method to load a single material to the shader
     *
     * @param i The index of the material
     * @param material The material to use
     */
    private void loadMaterial(int i, Material material) {
        assert i<MAX_MATERIALS;
        specularExponent[i].loadFloat(material.specularExponent);
        reflectivity[i].loadFloat(1f);
        ambientMult[i].loadVector(material.ambientMultiplier);
        diffuseMult[i].loadVector(material.diffuseMultiplier);
        specularMult[i].loadVector(material.specularMultiplier);
    }
}
