package engine.world;

import java.util.Random;

public class WorldGenerator {

    private long seed;
    private String seedString;

    /**
     * A helper class to generate psuedo-random smoothed heights
     *
     * @param seed The seed to use while generating the world
     */
    public WorldGenerator(String seed){
        this.seedString=seed;
        this.seed=new Random(seed.length()).nextLong();
        char[] chars = seed.toCharArray();
        int asciiSum=0;
        for(char c:chars)
            asciiSum+=c;
        int offset = new Random(asciiSum*asciiSum).nextInt();
        if(offset<0)offset=-offset;
        for(int i = 0; i < chars.length || i < 8; i++){
            char c = chars[(i+offset)%chars.length];
            for(int j=0;j<8;j++) {
                int x = (j+offset)%8;
                char cVal = c;
                if(x%2==0)
                    cVal = (char) ~ cVal;
                boolean flip = (cVal & (1 << j))!=0;
                if(flip)
                    this.seed = XOR_bit(this.seed, x*8+i%8);
            }
        }
    }

    /**
     * Gets the average height of a point by sampling an area around it
     *
     * @param x The x-coordinate to sample
     * @param y The y-coordinate to sample
     * @return The averaged height of the sample ranging from 0-1
     */
    public float getSmoothedHeight(int x, int y){
        float z = 0;
        for(int i=-4;i<=4;i++)
            for(int j=-4;j<=4;j++)
                z += getRawHeightNormalized(x+i, y+j);
        return z/81;
    }

    /**
     * The raw height of a sample point
     *
     * @param xPos The x-coordinate to sample
     * @param yPos The y-coordinate to sample
     * @return The raw height of the sample ranging from 0-1
     */
    private float getRawHeightNormalized(int xPos, int yPos){
        float x = xPos/100f+.0001f;
        float y = yPos/100f+.0002f;
        float z = 0;
        z += (float) Math.sin(seed/1E9+x+y*x);
        z += (float) Math.sin(seed/1E9+x+y*x*x/y+y);
        z += (float) Math.sin(seed/1E9+x*x+y/x);
        z += (float) Math.sin(seed/1E9+x*(y-x)/x+y+y*x*(1-x));
        return z/8f+.5f;
    }

    /**
     * Helper function to XOR a bit, used to generate a random number from a String
     *
     * @param val The long that you will be XORing the bit
     * @param bit The bit position to XOR
     * @return The original long with bit flipped
     */
    private static long XOR_bit(long val, int bit){
        long bitVal = 1<<bit;
        long lastBit = (val & (1<<bit));
        val &= ~(1<<bit);
        bitVal^=lastBit;
        val |= bitVal;
        return val;
    }

}
