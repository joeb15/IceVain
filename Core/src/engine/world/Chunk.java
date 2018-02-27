package engine.world;

import engine.render.models.RawModel;
import engine.utils.Loader;

public class Chunk {

    public static final int CHUNK_SIZE = 32;
    public static final int CHUNK_SIZE_PLUS_ONE = 33;
    public static final int CHUNK_HEIGHT = 32;

    private float[] vertices = new float[CHUNK_SIZE_PLUS_ONE*CHUNK_SIZE_PLUS_ONE*3];
    private float[] textures = new float[CHUNK_SIZE_PLUS_ONE*CHUNK_SIZE_PLUS_ONE*2];
    private float[] normals = new float[CHUNK_SIZE_PLUS_ONE*CHUNK_SIZE_PLUS_ONE*3];
    private int[] indices = new int[(CHUNK_SIZE)*(CHUNK_SIZE)*6];

    private float[][] heights = new float[CHUNK_SIZE_PLUS_ONE][CHUNK_SIZE_PLUS_ONE];

    private RawModel rawModel = null;

    /**
     * Generates a chunk
     *
     * @param worldGenerator The worldgenerator used to generate the chunk
     * @param x The chunk's x-coordinate
     * @param y The chunk's y-coordinate
     */
    public Chunk(WorldGenerator worldGenerator, int x, int y) {
        for(int i=0;i<CHUNK_SIZE_PLUS_ONE;i++){
            for(int j=0;j<CHUNK_SIZE_PLUS_ONE;j++){
                float zNorm = worldGenerator.getSmoothedHeight( x*CHUNK_SIZE+i, y*CHUNK_SIZE+j);
                heights[j][i]=zNorm*CHUNK_HEIGHT;

                vertices[(j * CHUNK_SIZE_PLUS_ONE + i) * 3] = x * CHUNK_SIZE + i;
                vertices[(j * CHUNK_SIZE_PLUS_ONE + i) * 3 + 1] = heights[j][i];
                vertices[(j * CHUNK_SIZE_PLUS_ONE + i) * 3 + 2] = y * CHUNK_SIZE + j;

                textures[(j * CHUNK_SIZE_PLUS_ONE + i) * 2] = i / (float) CHUNK_SIZE;
                textures[(j * CHUNK_SIZE_PLUS_ONE + i) * 2 + 1] = j / (float) CHUNK_SIZE;

                normals[(j * CHUNK_SIZE_PLUS_ONE + i) * 3] = 0;
                normals[(j * CHUNK_SIZE_PLUS_ONE + i) * 3 + 1] = 1;
                normals[(j * CHUNK_SIZE_PLUS_ONE + i) * 3 + 2] = 0;
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

    /**
     * Gets the model associated with the chunk
     *
     * @return The <code>RawModel</code> that represents the chunk
     */
    public RawModel getModel() {
        if(rawModel==null)
            rawModel = Loader.loadToVAO(vertices, normals, textures, indices);
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
