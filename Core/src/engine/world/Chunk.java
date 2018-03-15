package engine.world;

public class Chunk {

    public static final int CHUNK_SIZE = 32;
    public static final int CHUNK_HEIGHT = 32;

    private boolean shouldGenBelow = false;
    private boolean shouldGenAbove = false;

    private int[][][] blocks = new int[CHUNK_HEIGHT][CHUNK_SIZE][CHUNK_SIZE];
    private boolean[][][] render = new boolean[CHUNK_HEIGHT][CHUNK_SIZE][CHUNK_SIZE];

    private WorldGenerator worldGenerator;

    /**
     * Generates a chunk
     *
     * @param worldGenerator The worldgenerator used to generate the chunk
     * @param x The chunk's x-coordinate
     * @param y The chunk's y-coordinate
     * @param z The chunk's z-coordinate
     */
    public Chunk(WorldGenerator worldGenerator, int x, int y, int z) {
        this.worldGenerator=worldGenerator;
        for(int i=0;i<CHUNK_SIZE;i++){
            int xPos = i+x*CHUNK_SIZE;
            for(int j=0;j<CHUNK_SIZE;j++){
                int zPos = j+z*CHUNK_SIZE;
                int height = (int) (CHUNK_HEIGHT*worldGenerator.getRealisticHeight(xPos, zPos));
                for(int k=0;k<CHUNK_HEIGHT;k++){
                    int yPos = k+y*CHUNK_HEIGHT;
                    if(yPos<=height){
                        if(k==CHUNK_HEIGHT-1)
                            shouldGenAbove=true;
                        blocks[k][j][i] = 1;
                    }else{
                        if(k==0)
                            shouldGenBelow=true;
                        blocks[k][j][i] = 0;
                    }
                }
            }
        }
        for(int i=0;i<CHUNK_SIZE;i++)
           for(int j=0;j<CHUNK_SIZE;j++)
                for(int k=0;k<CHUNK_HEIGHT;k++) {
                    render[k][j][i] = blocks[k][j][i] != 0 && (isAir(i, k + 1, j) || isAir(i, k - 1, j) || isAir(i + 1, k, j) || isAir(i - 1, k, j) || isAir(i, k, j + 1) || isAir(i, k, j - 1));
                }

    }

    private boolean isAir(int x, int y, int z) {
        return x >= 0 && y >= 0 && z >= 0 && x < CHUNK_SIZE && y < CHUNK_HEIGHT && z < CHUNK_SIZE && blocks[y][z][x] == 0;
    }

    public boolean shouldRender(int x, int y, int z){
        return render[y][z][x];
    }

    public int getBlockID(int x, int y, int z){
        if(y>CHUNK_HEIGHT)
            return 0;
        return blocks[y][z][x];
    }

    public boolean shouldGenerateBelow() {
        return shouldGenAbove;
    }

    public boolean shouldGenerateAbove() {
        return shouldGenAbove;
    }
}
