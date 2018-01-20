package engine.physics;

import engine.entities.Entity;

import java.util.ArrayList;

public class World {

    private ArrayList<Entity> entities;

    public World(){
        entities = new ArrayList<>();
    }

    public void addEntity(Entity entity){
        entities.add(entity);
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }
}
