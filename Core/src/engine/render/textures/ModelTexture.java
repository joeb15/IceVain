package engine.render.textures;

import java.util.HashMap;

public class ModelTexture {

    private Texture texture;

    private static HashMap<String, ModelTexture> modelTextureHashMap = new HashMap<>();

    /**
     * A texture to be displayed on a model
     *
     * @param texture The texture to be rendered
     */
    public ModelTexture(Texture texture) {
        this.texture = texture;
    }

    /**
     * Getter for the model's texture
     *
     * @return The model's current texture
     */
    public Texture getTexture() {
        return texture;
    }

    /**
     * Getter for model textures from a hashmap
     *
     * @param texture The name of the texture file
     * @return The <code>ModelTexture</code> associated with that texture file
     */
    public static ModelTexture get(String texture) {
        if(!modelTextureHashMap.containsKey(texture))
            modelTextureHashMap.put(texture, new ModelTexture(new Texture(texture)));
        return modelTextureHashMap.get(texture);
    }
}
