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
     * Gets a height that is transformed into larger hills and smaller bumps
     *
     * @param x The x coordinate of the height
     * @param z The z coordinate of the height
     * @return The height
     */
    public float getRealisticHeight(float x, float z) {
        return (getHeight(x / 64f, z / 64f) * 16f + getHeight(x / 8f, z / 8f) * 7f + getHeight(x/4f, z/4f) * 2f) / 25f;
    }

    /**
     * Gets the height of a given point that is smoothed
     *
     * @param x The x coordinate of the smoothed height
     * @param z The z coordinate of the smoothed height
     * @return The smoothed random value
     */
    private float getHeight(float x, float z) {
        float corners = (getHeightAt(x + 1, z + 1) + getHeightAt(x + 1, z - 1) + getHeightAt(x - 1, z + 1)
                + getHeightAt(x - 1, z - 1)) / 4f;
        float sides = (getHeightAt(x + 1, z) + getHeightAt(x - 1, z) + getHeightAt(x, z + 1) + getHeightAt(x, z - 1))
                / 4f;
        return getHeightAt(x, z) / 4f + sides / 2f + corners / 4f;
    }

    /**
     * Gets a pseudo-random value
     *
     * @param x The x position of the pseudo random value
     * @param z The z position of the pseudo random value
     * @return The pseudo random value
     */
    private float getSrand(int x, int z) {
        return new Random(seed - (164*x-72*z)*(350*x+765*z)+x*x*(11+z)).nextFloat();
    }

    /**
     * Gets the height at certain coords
     *
     * @param x The x coordinate to sample
     * @param z The z coordinate to sample
     * @return The height at the sample value
     */
    private float getHeightAt(float x, float z) {
        int xPos = (int) x;
        int zPos = (int) z;
        if (x <= 0)
            xPos--;
        if (z <= 0)
            zPos--;
        float xFrac = x - xPos;
        float zFrac = z - zPos;
        float ul = getSrand(xPos, zPos);
        float ur = getSrand(xPos, zPos + 1);
        float ll = getSrand(xPos + 1, zPos);
        float lr = getSrand(xPos + 1, zPos + 1);
        float upper = ul * (1 - zFrac) + ur * zFrac;
        float lower = ll * (1 - zFrac) + lr * zFrac;
        return upper * (1 - xFrac) + lower * xFrac;
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
