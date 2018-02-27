package engine.render.models;

import engine.render.textures.ModelTexture;
import engine.utils.Loader;
import engine.utils.VFS;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class OBJLoader {

    public static HashMap<String, TexturedModel> texturedModelHashMap = new HashMap<>();

    /**
     * Loads all the information from a given file
     *
     * @param file The path of the file
     * @return The <code>RawModel</code> representing this data
     */
    public static RawModel loadModel(String file){
        File f = VFS.getFile(file);
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(f));
            String line;
            float[] texturesArray = null;
            float[] normalArray = null;
            ArrayList<Vector3f> vertices = new ArrayList<>();
            ArrayList<Vector2f> textures = new ArrayList<>();
            ArrayList<Vector3f> normals = new ArrayList<>();
            ArrayList<Integer> indices = new ArrayList<>();
            while((line=bufferedReader.readLine())!=null){
                line = line.replaceAll("  ", " ");
                String[] parts = line.split(" ");
                if(line.startsWith("v ")){
                    Vector3f vert = new Vector3f(Float.parseFloat(parts[1]),Float.parseFloat(parts[2]),Float.parseFloat(parts[3]));
                    vertices.add(vert);
                }else if(line.startsWith("vt ")){
                    Vector2f text = new Vector2f(Float.parseFloat(parts[1]),Float.parseFloat(parts[2]));
                    textures.add(text);
                }else if(line.startsWith("vn ")){
                    Vector3f norm = new Vector3f(Float.parseFloat(parts[1]),Float.parseFloat(parts[2]),Float.parseFloat(parts[3]));
                    normals.add(norm);
                }else if(line.startsWith("f ")){
                    texturesArray = new float[vertices.size()*2];
                    normalArray = new float[vertices.size()*3];
                    break;
                }
            }
            do{
                assert line != null;
                line = line.replaceAll("  ", " ");
                String[] parts = line.split(" ");
                if(line.startsWith("f ")){
                    processVertex(parts[1].split("/"), indices, textures, normals, texturesArray, normalArray);
                    processVertex(parts[2].split("/"), indices, textures, normals, texturesArray, normalArray);
                    processVertex(parts[3].split("/"), indices, textures, normals, texturesArray, normalArray);
                    for(int i=4;i<parts.length;++i){
                        processVertex(parts[i-3].split("/"), indices, textures, normals, texturesArray, normalArray);
                        processVertex(parts[i-1].split("/"), indices, textures, normals, texturesArray, normalArray);
                        processVertex(parts[i].split("/"), indices, textures, normals, texturesArray, normalArray);
                    }
                }
            }while((line=bufferedReader.readLine())!=null);
            bufferedReader.close();

            float[] verticesArray = new float[vertices.size()*3];
            int[] indicesArray = new int[indices.size()];
            int vertexPointer=0;
            for(Vector3f vertex:vertices){
                verticesArray[vertexPointer++] = vertex.x;
                verticesArray[vertexPointer++] = vertex.y;
                verticesArray[vertexPointer++] = vertex.z;
            }
            for(int i=0;i<indices.size();i++){
                indicesArray[i] = indices.get(i);
            }
            return Loader.loadToVAO(verticesArray, normalArray, texturesArray, indicesArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Processes a vertex and puts it into its respective arrays
     *
     * @param vertexData The string array of data that contains the vertex, texture and normal pointers for a specific vertex
     * @param indices The list of index data that has been parsed from the obj file
     * @param textures The list of texture data that has been parsed from the obj file
     * @param normals The list of normal data that has been parsed from the obj file
     * @param texturesArray The float array that the texture data will be put into
     * @param normalsArray The float array that the normal data will be put into
     */
    private static void processVertex(String[] vertexData, ArrayList<Integer> indices, ArrayList<Vector2f> textures, ArrayList<Vector3f> normals, float[] texturesArray, float[] normalsArray){
        int currPointer = Integer.parseInt(vertexData[0])-1;
        indices.add(currPointer);
        Vector2f currTex = textures.get(Integer.parseInt(vertexData[1])-1);
        texturesArray[currPointer*2] = currTex.x;
        texturesArray[currPointer*2+1] = 1-currTex.y;
        Vector3f currNorm = normals.get(Integer.parseInt(vertexData[2])-1);
        normalsArray[currPointer*3] = currNorm.x;
        normalsArray[currPointer*3+1] = currNorm.y;
        normalsArray[currPointer*3+2] = currNorm.z;
    }

    /**
     * Getter for the textured model associated with this entity name
     *
     * @param entityName The name of the entity as defined in the .ive files
     * @return The <code>TexturedModel</code> associated with the given entity name
     */
    public static TexturedModel getTexturedModel(String entityName){
        return texturedModelHashMap.get(entityName);
    }

    /**
     * Loads all entities that are defined from .ive files within the /resources directory
     */
    public static void loadEntities() {
        File[] iveFiles = VFS.getFilesWithStringRecursive(VFS.getFile("/resources/"), ".ive");
        if(iveFiles==null)
            return;
        for(File iveFile:iveFiles) {
            if (texturedModelHashMap == null)
                texturedModelHashMap = new HashMap<>();
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(iveFile));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    if (line.startsWith("#"))
                        continue;
                    String[] parts = line.split(" ");
                    if (parts.length >= 3) {
                        String name = parts[0];
                        String model = parts[1];
                        String texture = parts[2];
                        RawModel rawModel = OBJLoader.loadModel(model);
                        ModelTexture modelTexture = ModelTexture.get(texture);
                        TexturedModel texturedModel = new TexturedModel(rawModel, modelTexture);
                        for(int i=3;i<parts.length;i++){
                            String[] attribs = parts[i].split(";",2);
                            switch (attribs[0]){
                                case "shine":
                                    texturedModel.setShine(Float.parseFloat(attribs[1]));
                                    break;
                                case "reflectivity":
                                    texturedModel.setReflectivity(Float.parseFloat(attribs[1]));
                                    break;
                            }
                        }
                        texturedModelHashMap.put(name, texturedModel);
                    }
                }
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
