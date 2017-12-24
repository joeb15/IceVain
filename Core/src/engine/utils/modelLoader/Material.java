package engine.utils.modelLoader;

import org.joml.Vector3f;

public class Material {
    public String name;
    public Vector3f ambient = new Vector3f(), diffuse = new Vector3f(), specular = new Vector3f();
    public float specularExponent=1;
}
