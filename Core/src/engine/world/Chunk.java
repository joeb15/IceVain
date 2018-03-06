package engine.world;

import engine.render.models.RawModel;
import engine.utils.Loader;
import org.joml.Vector3f;

public class Chunk {

    public static final int CHUNK_SIZE = 32;
    public static final int CHUNK_SIZE_PLUS_ONE = 33;
    public static final int CHUNK_HEIGHT = 32;

    private float[] vertices = new float[CHUNK_SIZE_PLUS_ONE*CHUNK_SIZE_PLUS_ONE*3];
    private float[] textures = new float[CHUNK_SIZE_PLUS_ONE*CHUNK_SIZE_PLUS_ONE*2];
    private float[] normals = new float[CHUNK_SIZE_PLUS_ONE*CHUNK_SIZE_PLUS_ONE*3];
    private float[] tangents = new float[CHUNK_SIZE_PLUS_ONE*CHUNK_SIZE_PLUS_ONE*3];
    private int[] indices = new int[(CHUNK_SIZE)*(CHUNK_SIZE)*6];
    private int[] materials = new int[CHUNK_SIZE_PLUS_ONE*CHUNK_SIZE_PLUS_ONE];

    private float[][] heights = new float[CHUNK_SIZE_PLUS_ONE][CHUNK_SIZE_PLUS_ONE];

    private RawModel rawModel = null;
    private WorldGenerator worldGenerator;

    /**
     * Generates a chunk
     *
     * @param worldGenerator The worldgenerator used to generate the chunk
     * @param x The chunk's x-coordinate
     * @param y The chunk's y-coordinate
     */
    public Chunk(WorldGenerator worldGenerator, int x, int y) {
        this.worldGenerator=worldGenerator;
        for(int i=0;i<CHUNK_SIZE_PLUS_ONE;i++){
            for(int j=0;j<CHUNK_SIZE_PLUS_ONE;j++){
                float zNorm = worldGenerator.getRealisticHeight( x*CHUNK_SIZE+i, y*CHUNK_SIZE+j);
                heights[j][i]=zNorm*CHUNK_HEIGHT;

                vertices[(j * CHUNK_SIZE_PLUS_ONE + i) * 3] = x * CHUNK_SIZE + i;
                vertices[(j * CHUNK_SIZE_PLUS_ONE + i) * 3 + 1] = heights[j][i];
                vertices[(j * CHUNK_SIZE_PLUS_ONE + i) * 3 + 2] = y * CHUNK_SIZE + j;

                textures[(j * CHUNK_SIZE_PLUS_ONE + i) * 2] = i / (float) CHUNK_SIZE;
                textures[(j * CHUNK_SIZE_PLUS_ONE + i) * 2 + 1] = j / (float) CHUNK_SIZE;

                Vector3f normal = calculateNormal(x*CHUNK_SIZE+i, y*CHUNK_SIZE+j);

                normals[(j * CHUNK_SIZE_PLUS_ONE + i) * 3] = normal.x;
                normals[(j * CHUNK_SIZE_PLUS_ONE + i) * 3 + 1] = normal.y;
                normals[(j * CHUNK_SIZE_PLUS_ONE + i) * 3 + 2] = normal.z;

                Vector3f tangent = calculateTangent(normal);

                tangents[(j * CHUNK_SIZE_PLUS_ONE + i) * 3] = tangent.x;
                tangents[(j * CHUNK_SIZE_PLUS_ONE + i) * 3 + 1] = tangent.y;
                tangents[(j * CHUNK_SIZE_PLUS_ONE + i) * 3 + 2] = tangent.z;

                materials[(j* CHUNK_SIZE_PLUS_ONE + i )] = 0;
            }
        }
        int index=0;
        for (int j = 0; j < CHUNK_SIZE; j++) {
            for(int i = 0; i < CHUNK_SIZE; i++) {
                indices[index++] = (j+1) * CHUNK_SIZE_PLUS_ONE + (i+1);
                indices[index++] = (j) * CHUNK_SIZE_PLUS_ONE + (i+1);
                indices[index++] = (j+1) * CHUNK_SIZE_PLUS_ONE + (i);
                indices[index++] = (j+1) * CHUNK_SIZE_PLUS_ONE + (i);
                indices[index++] = (j) * CHUNK_SIZE_PLUS_ONE + (i+1);
                indices[index++] = (j) * CHUNK_SIZE_PLUS_ONE + (i);
            }
        }
    }

    public Vector3f calculateTangent(Vector3f normal){
        Vector3f right = new Vector3f(0,0,1);
        return normal.cross(right, new Vector3f());
    }

    /**
     * Calculates the normal of a given point using the upper right and lower left triangles to determine the vertex normal
     *
     * @param x The x position of the vertex
     * @param y The y position of the vertex
     * @return The vector calculation of the normal
     */
    public Vector3f calculateNormal(int x, int y){
        float heightx0y0 = worldGenerator.getRealisticHeight(x, y) * CHUNK_HEIGHT;
        float heightx1y0 = worldGenerator.getRealisticHeight(x+1, y) * CHUNK_HEIGHT;
        float heightx0y1 = worldGenerator.getRealisticHeight(x, y+1) * CHUNK_HEIGHT;
        float heightxn1y0 = worldGenerator.getRealisticHeight(x-1, y) * CHUNK_HEIGHT;
        float heightx0yn1 = worldGenerator.getRealisticHeight(x, y-1) * CHUNK_HEIGHT;
        Vector3f deltaZP = new Vector3f(0,heightx0y1-heightx0y0,1);
        Vector3f deltaXP = new Vector3f(1,heightx1y0-heightx0y0,0);
        Vector3f deltaZN = new Vector3f(0,heightx0yn1-heightx0y0,-1);
        Vector3f deltaXN = new Vector3f(-1,heightxn1y0-heightx0y0,0);
        Vector3f posNorm = deltaZP.cross(deltaXP, new Vector3f());
        Vector3f negNorm = deltaZN.cross(deltaXN, new Vector3f());
        return posNorm.add(negNorm, new Vector3f()).normalize();
    }

    /**
     * Gets the model associated with the chunk
     *
     * @return The <code>RawModel</code> that represents the chunk
     */
    public RawModel getModel() {
        if(rawModel==null)
            rawModel = Loader.loadToVAO(vertices, normals, tangents, textures, indices, materials);
        return rawModel;
    }

    /**
     * Gets the height at a certain position
     *
     * @param x The x position to sample the height from
     * @param y The y position to sample the height from
     * @return The height at the sample coordinates
     */
    public float getHeight(int x, int y) {
        return heights[y][x];
    }

}
