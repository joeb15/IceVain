package engine.render.models;

import org.joml.Vector3f;

/**
 * Storage class for .mtl file materials
 */
public class Material {

    private static final Material defaultMaterial = new Material();

    public int id = 0;
    public float specularExponent = 100;
    public Vector3f ambientMultiplier = new Vector3f(1,1,1);
    public Vector3f diffuseMultiplier = new Vector3f(1,1,1);
    public Vector3f specularMultiplier = new Vector3f(1,1,1);
    public float indexOfRefraction = 1;
    public float dissolve = 1;
    public int illiumValue = 1;
    public String diffuseTexture = "/resources/UnknownTexture.jpg";
    public String normalTexture = "/resources/UnknownNormal.jpg";
    public String specularTexture = "/resources/UnknownTexture.jpg";

    /**
     * Gets the default material
     *
     * @return The default material
     */
    public static Material getDefaultMaterial(){
        return new Material();
    }
}
