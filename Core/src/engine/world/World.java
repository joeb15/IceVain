package engine.world;

import engine.entities.Entity;
import engine.render.models.MaterialLibrary;
import org.joml.Vector3i;

import java.util.ArrayList;
import java.util.HashMap;

import static engine.world.Chunk.CHUNK_HEIGHT;

public class World {

    private ArrayList<Entity> entities;
    private WorldGenerator worldGenerator;
    private MaterialLibrary materialLibrary;

    private HashMap<Vector3i, Chunk> chunkHashMap;
    private HashMap<Vector3i, Thread> chunkThreads;

    /**
     * Generates a new world given a seed
     *
     * @param seed The seed to use when generating the new world
     */
    public World(String seed) {
        materialLibrary = MaterialLibrary.createDefault();
        materialLibrary.getMaterial(0).diffuseTexture = "/resources/cobblestone/diffuse.png";
        worldGenerator = new WorldGenerator(seed);
        entities = new ArrayList<>();
        chunkHashMap = new HashMap<>();
        chunkThreads = new HashMap<>();
    }

    /**
     * Generates a new chunk
     *
     * @param x The chunk's X position
     * @param y The chunk's Y position
     * @param z The chunk's Z position
     */
    public void generateChunk(int x, int y, int z){
        Vector3i pos = new Vector3i(x, y, z);
        if(!chunkThreads.containsKey(pos)) {
            Thread t = new Thread(() -> {
                Chunk chunk = new Chunk(worldGenerator, x, y, z);
                chunkHashMap.put(pos, chunk);
                if(chunk.shouldGenerateAbove())
                    generateChunk(x, y+1, z);
                if(chunk.shouldGenerateBelow())
                    generateChunk(x, y-1, z);
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
     * @param z The chunk's Z position
     * @return The chunk, or null if the chunk hasn't been generated yet
     */
    public Chunk getChunk(int x, int y, int z){
        Vector3i pos = new Vector3i(x, y, z);
        if(!chunkHashMap.containsKey(pos)) {
            generateChunk(x, y, z);
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
     * Getter method for the <code>ArrayList</code> of entitites in the world
     *
     * @return The <code>ArrayList</code> containing all of the entities
     */
    public ArrayList<Entity> getEntities() {
        return entities;
    }

    /**
     * Clears all of the entities in the world
     */
    public void clearEntities() {
        entities.clear();
    }

    /**
     * Gets the height of the world at any point
     *
     * @param x The x-coordinate of the sample
     * @param y The y-coordinate of the sample
     * @return The height of the sample point as a float between 0 and <code>Chunk.CHUNK_HEIGHT</code>
     */
    public float getHeight(float x, float y) {
        return worldGenerator.getRealisticHeight(x, y)*CHUNK_HEIGHT;
    }

    /**
     * Getter for the material library for the world
     *
     * @return The material library for the world
     */
    public MaterialLibrary getMaterialLibrary() {
        return materialLibrary;
    }

    /**
     * Gets an entity given its ID value
     *
     * @param id The id of the entity
     * @return The Entity associated with that ID
     */
    public Entity getEntity(int id) {
        for(Entity e:entities){
            if(e.id==id)
                return e;
        }
        return null;
    }
}
