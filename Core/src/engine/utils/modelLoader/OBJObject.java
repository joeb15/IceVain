package engine.utils.modelLoader;

import org.joml.Vector3f;

import java.util.ArrayList;

public class OBJObject {
    public String name;
    public ArrayList<Vector3f> vertices = new ArrayList<>();
    public ArrayList<Integer> indicies = new ArrayList<>();
    public ArrayList<Integer> materials = new ArrayList<>();
}
