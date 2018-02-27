package engine.world;

import engine.entities.Entity;
import engine.entities.Light;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.HashMap;

import static engine.world.Chunk.CHUNK_SIZE;

public class World {

    private ArrayList<Entity> entities;
    private ArrayList<Light> lights;
    private WorldGenerator worldGenerator;

    private HashMap<Vector2i, Chunk> chunkHashMap;
    private HashMap<Vector2i, Thread> chunkThreads;

    /**
     * Generates a new world given a seed
     *
     * @param seed The seed to use when generating the new world
     */
    public World(String seed) {
        worldGenerator = new WorldGenerator(seed);
        lights = new ArrayList<>();
        entities = new ArrayList<>();
        chunkHashMap = new HashMap<>();
        chunkThreads = new HashMap<>();
    }

    /**
     * Generates a new chunk
     *
     * @param x The chunk's X position
     * @param y The chunk's Y position
     */
    public void generateChunk(int x, int y){
        Vector2i pos = new Vector2i(x, y);
        if(!chunkThreads.containsKey(pos)) {
            Thread t = new Thread(() -> {
                chunkHashMap.put(pos, new Chunk(worldGenerator, x, y));
                chunkThreads.remove(pos);
            });
            chunkThreads.put(pos, t);
            t.start();
        }
    }

    /**
     * Gets the chunk at certain coords, and generates it if it is not loaded
     *
     * @param x The chunk's X position
     * @param y The chunk's Y position
     * @return The chunk, or null if the chunk hasn't been generated yet
     */
    public Chunk getChunk(int x, int y){
        Vector2i pos = new Vector2i(x, y);
        if(!chunkHashMap.containsKey(pos)) {
            generateChunk(x, y);
            return null;
        }
        return chunkHashMap.get(pos);
    }

    /**
     * Adds an entity to the world
     *
     * @param entity The entity to add
     */
    public void addEntity(Entity entity){
        entities.add(entity);
    }

    /**
     * Adds a light to the world
     *
     * @param light The light to add to the world
     */
    public void addLight(Light light){
        lights.add(light);
    }

    /**
     * Getter method for the <code>ArrayList</code> of entitites in the world
     *
     * @return The <code>ArrayList</code> containing all of the entities
     */
    public ArrayList<Entity> getEntities() {
        return entities;
    }

    /**
     * Getter method for the <code>ArrayList</code> of lights in the world
     *
     * @return The <code>ArrayList</code> containing all of the lights
     */
    public ArrayList<Light> getLights() {
        return lights;
    }

    /**
     * Clears all of the entities and lights in the world
     */
    public void clearEntities() {
        entities.clear();
        lights.clear();
    }

    /**
     * Gets the height of the world at any point
     *
     * @param x The x-coordinate of the sample
     * @param y The y-coordinate of the sample
     * @return The height of the sample point as a float between 0 and <code>Chunk.CHUNK_HEIGHT</code>
     */
    public float getHeight(int x, int y) {
        int chunkX = x / CHUNK_SIZE;
        int chunkY = y / CHUNK_SIZE;
        Chunk c = chunkHashMap.get(new Vector2i(chunkX, chunkY));
        if(c==null)
            return 0;
        return c.getHeight(x%CHUNK_SIZE, y%CHUNK_SIZE);
    }
}
