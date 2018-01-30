package engine.render.shaders;

public class UniformStruct {
    private Uniform[] parts;

    public UniformStruct(int ... locations){
        parts = new Uniform[locations.length];
        for(int i=0;i<parts.length;i++){
            parts[i] = new Uniform(locations[i]);
        }
    }

    public Uniform getUniform(int pos) {
        if(pos>=0 && pos<parts.length)
            return parts[pos];
        return null;
    }
}
