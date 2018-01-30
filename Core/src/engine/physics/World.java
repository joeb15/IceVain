package engine.physics;

import engine.entities.Entity;
import engine.entities.Light;

import java.util.ArrayList;

public class World {

    private ArrayList<Entity> entities;
    private ArrayList<Light> lights;

    public World() {
        lights = new ArrayList<>();
        entities = new ArrayList<>();
    }

    public void addEntity(Entity entity){
        entities.add(entity);
    }

    public void addLight(Light light){
        lights.add(light);
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }

    public ArrayList<Light> getLights() {
        return lights;
    }
}
