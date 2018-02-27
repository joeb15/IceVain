package engine.render.shaders;

public class UniformStruct {
    private Uniform[] parts;

    /**
     * An interface for uniform structs within the shader
     *
     * @param locations The locations of the components of the structs
     */
    public UniformStruct(int ... locations){
        parts = new Uniform[locations.length];
        for(int i=0;i<parts.length;i++){
            parts[i] = new Uniform(locations[i]);
        }
    }

    /**
     * Getter for the uniform variables at a certain position
     *
     * @param pos The location of the uniform
     * @return The uniform associated with that position
     */
    public Uniform getUniform(int pos) {
        if(pos>=0 && pos<parts.length)
            return parts[pos];
        return null;
    }
}
