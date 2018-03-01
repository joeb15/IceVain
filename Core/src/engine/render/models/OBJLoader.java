package engine.render.models;

import engine.render.textures.Texture;
import engine.utils.Debug;
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
     * @return The <code>TexturedModel</code> representing this data
     */
    public static TexturedModel loadModel(String file){
        File f = VFS.getFile(file);
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(f));
            String line;
            float[] texturesArray = null;
            float[] normalArray = null;
            float[] tangentArray = null;
            int[] materialsArray = null;
            ArrayList<Vector3f> vertices = new ArrayList<>();
            ArrayList<Vector2f> textures = new ArrayList<>();
            ArrayList<Vector3f> normals = new ArrayList<>();
            ArrayList<Integer> indices = new ArrayList<>();
            MaterialLibrary materialLibrary = null;
            while((line=bufferedReader.readLine())!=null){
                line = line.replaceAll("  ", " ");
                String[] parts = line.split(" ");
                if(line.startsWith("mtllib ")){
                    File mtlFile = VFS.getFileInSameFolder(f, parts[1]);
                    if(mtlFile.exists())
                        materialLibrary = new MaterialLibrary(mtlFile);
                }else if(line.startsWith("v ")){
                    Vector3f vert = new Vector3f(Float.parseFloat(parts[1]),Float.parseFloat(parts[2]),Float.parseFloat(parts[3]));
                    vertices.add(vert);
                }else if(line.startsWith("vt ")){
                    Vector2f text = new Vector2f(Float.parseFloat(parts[1]),Float.parseFloat(parts[2]));
                    textures.add(text);
                }else if(line.startsWith("vn ")){
                    Vector3f norm = new Vector3f(Float.parseFloat(parts[1]),Float.parseFloat(parts[2]),Float.parseFloat(parts[3]));
                    normals.add(norm);
                }else if(line.startsWith("f ") || line.startsWith("usemtl ") || line.startsWith("s ")){
                    texturesArray = new float[vertices.size()*2];
                    normalArray = new float[vertices.size()*3];
                    tangentArray = new float[vertices.size()*3];
                    materialsArray = new int[vertices.size()];
                    break;
                }
            }
            int mtlID = 0;
            do{
                assert line != null;
                line = line.replaceAll("  ", " ");
                String[] parts = line.split(" ");
                if(line.startsWith("f ")){
                    processFace(parts[1].split("/"), parts[2].split("/"), parts[3].split("/"), vertices, indices, textures, normals, texturesArray, normalArray, materialsArray, tangentArray, mtlID);
//                    processVertex(parts[1].split("/"), indices, textures, normals, texturesArray, normalArray, materialsArray, mtlID);
//                    processVertex(parts[2].split("/"), indices, textures, normals, texturesArray, normalArray, materialsArray, mtlID);
//                    processVertex(parts[3].split("/"), indices, textures, normals, texturesArray, normalArray, materialsArray, mtlID);
                    for(int i=4;i<parts.length;++i){
                        processVertex(parts[i-3].split("/"), indices, textures, normals, texturesArray, normalArray, materialsArray, mtlID);
                        processVertex(parts[i-1].split("/"), indices, textures, normals, texturesArray, normalArray, materialsArray, mtlID);
                        processVertex(parts[i].split("/"), indices, textures, normals, texturesArray, normalArray, materialsArray, mtlID);
                    }
                }else if(line.startsWith("usemtl ")){
                    if(materialLibrary!=null)
                        mtlID = materialLibrary.getMaterialID(parts[1]);
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
            RawModel rawModel = Loader.loadToVAO(verticesArray, normalArray, tangentArray, texturesArray, indicesArray, materialsArray);
            TexturedModel texturedModel = new TexturedModel(rawModel);
            if(materialLibrary!=null)
                texturedModel.setMaterialLibrary(materialLibrary);
            else
                texturedModel.setMaterialLibrary(MaterialLibrary.createDefault());
            return texturedModel;
        } catch (IOException e) {
            e.printStackTrace();
        }
        Debug.error("Unable to load "+file);
        System.exit(0);
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
     * @param mtlID The ID of the material to use, or -1 to use default material
     */
    private static void processVertex(String[] vertexData, ArrayList<Integer> indices, ArrayList<Vector2f> textures, ArrayList<Vector3f> normals, float[] texturesArray, float[] normalsArray, int[] materialsArray, int mtlID){
        int currPointer = Integer.parseInt(vertexData[0])-1;
        indices.add(currPointer);
        Vector2f currTex = textures.get(Integer.parseInt(vertexData[1])-1);
        texturesArray[currPointer*2] = currTex.x;
        texturesArray[currPointer*2+1] = 1-currTex.y;
        Vector3f currNorm = normals.get(Integer.parseInt(vertexData[2])-1);
        normalsArray[currPointer*3] = currNorm.x;
        normalsArray[currPointer*3+1] = currNorm.y;
        normalsArray[currPointer*3+2] = currNorm.z;
        materialsArray[currPointer] = mtlID;
    }

    private static void processFace(String[] vertexData1, String[] vertexData2, String[] vertexData3, ArrayList<Vector3f> vertices, ArrayList<Integer> indices, ArrayList<Vector2f> textures, ArrayList<Vector3f> normals, float[] texturesArray, float[] normalsArray, int[] materialsArray, float[] tangentArray, int mtlID){
        //Vertex1
        int currPointer1 = Integer.parseInt(vertexData1[0])-1;
        indices.add(currPointer1);
        Vector2f currTex1 = textures.get(Integer.parseInt(vertexData1[1])-1);
        texturesArray[currPointer1*2] = currTex1.x;
        texturesArray[currPointer1*2+1] = 1-currTex1.y;
        Vector3f currNorm1 = normals.get(Integer.parseInt(vertexData1[2])-1);
        normalsArray[currPointer1*3] = currNorm1.x;
        normalsArray[currPointer1*3+1] = currNorm1.y;
        normalsArray[currPointer1*3+2] = currNorm1.z;
        materialsArray[currPointer1] = mtlID;

        //Vertex2
        int currPointer2 = Integer.parseInt(vertexData2[0])-1;
        indices.add(currPointer2);
        Vector2f currTex2 = textures.get(Integer.parseInt(vertexData2[1])-1);
        texturesArray[currPointer2*2] = currTex2.x;
        texturesArray[currPointer2*2+1] = 1-currTex2.y;
        Vector3f currNorm2 = normals.get(Integer.parseInt(vertexData2[2])-1);
        normalsArray[currPointer2*3] = currNorm2.x;
        normalsArray[currPointer2*3+1] = currNorm2.y;
        normalsArray[currPointer2*3+2] = currNorm2.z;
        materialsArray[currPointer2] = mtlID;

        //Vertex3
        int currPointer3 = Integer.parseInt(vertexData3[0])-1;
        indices.add(currPointer3);
        Vector2f currTex3 = textures.get(Integer.parseInt(vertexData3[1])-1);
        texturesArray[currPointer3*2] = currTex3.x;
        texturesArray[currPointer3*2+1] = 1-currTex3.y;
        Vector3f currNorm3 = normals.get(Integer.parseInt(vertexData3[2])-1);
        normalsArray[currPointer3*3] = currNorm3.x;
        normalsArray[currPointer3*3+1] = currNorm3.y;
        normalsArray[currPointer3*3+2] = currNorm3.z;
        materialsArray[currPointer3] = mtlID;

        //Tangent calculations
        Vector3f deltaPos1 = new Vector3f();
        Vector3f deltaPos2 = new Vector3f();
        vertices.get(currPointer2).sub(vertices.get(currPointer1), deltaPos1);
        vertices.get(currPointer3).sub(vertices.get(currPointer1), deltaPos2);
        Vector2f deltaUV1 = new Vector2f();
        Vector2f deltaUV2 = new Vector2f();
        currTex2.sub(currTex1, deltaUV1);
        currTex3.sub(currTex1, deltaUV2);

        float r = 1f/(deltaUV1.x*deltaUV2.y - deltaUV1.y*deltaUV2.x);
        Vector3f tangent = (deltaPos1.mul(deltaUV2.y).sub(deltaPos2.mul(deltaUV1.x))).mul(r);
        tangentArray[currPointer1*3] = tangent.x;
        tangentArray[currPointer2*3] = tangent.x;
        tangentArray[currPointer3*3] = tangent.x;
        tangentArray[currPointer1*3+1] = tangent.y;
        tangentArray[currPointer2*3+1] = tangent.y;
        tangentArray[currPointer3*3+1] = tangent.y;
        tangentArray[currPointer1*3+2] = tangent.z;
        tangentArray[currPointer2*3+2] = tangent.z;
        tangentArray[currPointer3*3+2] = tangent.z;
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
                    if (parts.length >= 2) {
                        int index = 0;
                        String name = parts[index++];
                        String model = parts[index++];
                        TexturedModel texturedModel = OBJLoader.loadModel(model);
                        assert texturedModel!=null;
                        if(!texturedModel.hasCustomMaterial()) {
                            String texture = parts[index++];
                            texturedModel.setTexture(Texture.getTexture(texture));
                        }
                        while (index<parts.length){
                            String[] attribs = parts[index++].split(";",2);
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
