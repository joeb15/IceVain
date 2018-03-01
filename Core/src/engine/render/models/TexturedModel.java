package engine.render.models;

import engine.render.textures.Texture;

public class TexturedModel {
    private RawModel model;
    private MaterialLibrary materialLibrary;

    /**
     * Creates a class that contains both model and texture data
     *
     * @param model The model to be drawn
     * @param texture The texture to draw the model with
     */
    public TexturedModel(RawModel model, Texture texture) {
        this.model = model;
        setTexture(texture);
    }

    /**
     * Creates a class that contains model data
     *
     * @param model The model to be drawn
     */
    public TexturedModel(RawModel model) {
        this.model = model;
    }

    /**
     * Sets the modelTexture to use for the texturedModel
     *
     * @param texture The new texture to use
     */
    public void setTexture(Texture texture){
        this.materialLibrary.getMaterial(0).diffuseTexture = texture.getPath();
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
     * Setter for the shine value of the gui
     *
     * @param shine The new shine value to be used
     */
    public void setShine(float shine) {
        for(Material m:materialLibrary.getMaterials())
            m.specularExponent = shine;
    }

    /**
     * Setter for the reflectivity of the model
     *
     * @param reflectivity The new reflectivity of the model
     */
    public void setReflectivity(float reflectivity) {
        for(Material m:materialLibrary.getMaterials()) {
            m.specularMultiplier.x = reflectivity;
            m.specularMultiplier.y = reflectivity;
            m.specularMultiplier.z = reflectivity;
        }
    }

    /**
     * Setter for the material library
     *
     * @param materialLibrary The material library to use
     */
    public void setMaterialLibrary(MaterialLibrary materialLibrary) {
        this.materialLibrary = materialLibrary;
    }

    /**
     * Getter for the material library associated with the textured model
     *
     * @return The material library currently being used
     */
    public MaterialLibrary getMaterialLibrary() {
        return materialLibrary;
    }

    /**
     * Returns whether a material library is custom or default
     *
     * @return Whether the library is custom
     */
    public boolean hasCustomMaterial() {
        return materialLibrary.isCustom();
    }
}
