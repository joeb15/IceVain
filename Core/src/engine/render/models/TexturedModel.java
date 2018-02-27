package engine.render.models;

import engine.render.textures.ModelTexture;

public class TexturedModel {
    private RawModel model;
    private ModelTexture texture;

    private float shine=5, reflectivity=0.1f;

    /**
     * Creates a class that contains both model and texture data
     *
     * @param model The model to be drawn
     * @param texture The texture to draw the model with
     */
    public TexturedModel(RawModel model, ModelTexture texture) {
        this.model = model;
        this.texture = texture;
    }

    /**
     * Getter for the model
     *
     * @return The current model being drawn
     */
    public RawModel getModel() {
        return model;
    }

    /**
     * Getter for the model's texture
     *
     * @return The texture being rendered onto the model
     */
    public ModelTexture getTexture() {
        return texture;
    }

    /**
     * Getter for the shine value of the gui
     *
     * @return The current shine value of the model
     */
    public float getShine() {
        return shine;
    }

    /**
     * Setter for the shine value of the gui
     *
     * @param shine The new shine value to be used
     */
    public void setShine(float shine) {
        this.shine = shine;
    }

    /**
     * Getter for the reflectivity of the model
     *
     * @return The current reflectivity value
     */
    public float getReflectivity() {
        return reflectivity;
    }

    /**
     * Setter for the reflectivity of the model
     *
     * @param reflectivity The new reflectivity of the model
     */
    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }
}
