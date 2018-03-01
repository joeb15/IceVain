package engine.render.models;

import engine.render.textures.Texture;
import engine.utils.VFS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

public class MaterialLibrary {

    private HashMap<String, Material> materialHashMap = new HashMap<>();
    private boolean custom = true;

    /**
     * Generates a material library from a given file
     *
     * @param materialFile The .mtl file
     */
    public MaterialLibrary(File materialFile) {
        try{
            BufferedReader bufferedReader = new BufferedReader(new FileReader(materialFile));
            Material currentMaterial = null;
            String line;
            int materialID = 0;
            while((line=bufferedReader.readLine())!=null){
                String[] parts = line.split(" ");
                if(line.startsWith("newmtl")){
                    currentMaterial = new Material();
                    currentMaterial.id = materialID++;
                    materialHashMap.put(parts[1], currentMaterial);
                }else if(currentMaterial!=null){
                    if(line.startsWith("Ns ")){
                        currentMaterial.specularExponent = Float.parseFloat(parts[1]);
                    }else if(line.startsWith("Ka ")){
                        currentMaterial.ambientMultiplier.x = Float.parseFloat(parts[1]);
                        currentMaterial.ambientMultiplier.y = Float.parseFloat(parts[2]);
                        currentMaterial.ambientMultiplier.z = Float.parseFloat(parts[3]);
                    }else if(line.startsWith("Kd ")){
                        currentMaterial.diffuseMultiplier.x = Float.parseFloat(parts[1]);
                        currentMaterial.diffuseMultiplier.y = Float.parseFloat(parts[2]);
                        currentMaterial.diffuseMultiplier.z = Float.parseFloat(parts[3]);
                    }else if(line.startsWith("Ks ")){
                        currentMaterial.specularMultiplier.x = Float.parseFloat(parts[1]);
                        currentMaterial.specularMultiplier.y = Float.parseFloat(parts[2]);
                        currentMaterial.specularMultiplier.z = Float.parseFloat(parts[3]);
                    }else if(line.startsWith("Ni ")){
                        currentMaterial.indexOfRefraction = Float.parseFloat(parts[1]);
                    }else if(line.startsWith("d ")){
                        currentMaterial.dissolve = Float.parseFloat(parts[1]);
                    }else if(line.startsWith("illium ")){
                        currentMaterial.illiumValue = Integer.parseInt(parts[1]);
                    }else if(line.startsWith("map_Kd ")){
                        currentMaterial.diffuseTexture = VFS.getFileStringInSameFolder(materialFile, parts[1]);
                    }else if(line.startsWith("map_normal ")){
                        currentMaterial.normalTexture = VFS.getFileStringInSameFolder(materialFile, parts[1]);
                    }else if(line.startsWith("map_Ks ")){
                        currentMaterial.specularTexture = VFS.getFileStringInSameFolder(materialFile, parts[1]);
                    }
                }
            }
        }catch(IOException e){
            custom=false;
            materialHashMap.put("default", Material.getDefaultMaterial());
        }
    }

    /**
     * Creates a default material library
     */
    private MaterialLibrary() {
        custom=false;
        materialHashMap.put("default", Material.getDefaultMaterial());
    }

    /**
     * Getter for a materials ID
     *
     * @param material The name of the material
     * @return The id of the material, or -1 if it doesn't exist
     */
    public int getMaterialID(String material){
        if(!materialHashMap.containsKey(material))
            return -1;
        return materialHashMap.get(material).id;
    }

    /**
     * Returns all of the materials in the material library
     *
     * @return The Collection of materials
     */
    public Collection<Material> getMaterials() {
        return materialHashMap.values();
    }

    /**
     * Binds all the textures associated with the material
     */
    public void bindTextures(){
        for(Material material:materialHashMap.values()){
            Texture.getTexture(material.diffuseTexture).bind(material.id);
        }
    }

    /**
     * Binds all the normals associated with the material
     *
     * @param maxMaterials The texture index offset
     */
    public void bindNormals(int maxMaterials) {
        for(Material material:materialHashMap.values()){
            Texture.getTexture(material.normalTexture).bind(material.id + maxMaterials);
        }
    }

    /**
     * Binds all the normals associated with the material
     *
     * @param maxMaterials The texture index offset
     */
    public void bindSpecular(int maxMaterials) {
        for(Material material:materialHashMap.values()){
            Texture.getTexture(material.specularTexture).bind(material.id + maxMaterials);
        }
    }

    /**
     * Creates a default material library
     *
     * @return The default material library
     */
    public static MaterialLibrary createDefault() {
        return new MaterialLibrary();
    }

    /**
     * Gets a material from the library
     *
     * @param i The material ID of the material
     * @return The Material associated with that ID, or default material
     */
    public Material getMaterial(int i) {
        for(Material m:materialHashMap.values()){
            if(m.id==i){
                return m;
            }
        }
        if(materialHashMap.containsKey("default"))
            return materialHashMap.get("default");
        Material mtl = Material.getDefaultMaterial();
        materialHashMap.put("default", mtl);
        return mtl;
    }

    /**
     * Returns whether the library is custom
     *
     * @return Whether the library is custom
     */
    public boolean isCustom() {
        return custom;
    }
}
