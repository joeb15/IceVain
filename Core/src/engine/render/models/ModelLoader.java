package engine.render.models;

import engine.render.textures.Texture;
import engine.utils.Loader;
import engine.utils.VFS;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ModelLoader {

    public static HashMap<String, TexturedModel> texturedModelHashMap = new HashMap<>();

    /**
     * Loads a textured model from a file
     *
     * @param objFileName The path to the obj file
     * @return The textured model
     */
    public static TexturedModel loadModel(String objFileName) {
        FileReader isr = null;
        File objFile = VFS.getFile(objFileName);
        try {
            isr = new FileReader(objFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert isr != null;
        BufferedReader reader = new BufferedReader(isr);
        String line;
        List<VertexNM> vertices = new ArrayList<>();
        List<Vector2f> textures = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Integer> materials = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        MaterialLibrary materialLibrary = null;
        try {
            while (true) {
                line = reader.readLine();
                String[] currentLine = line.split(" ");
                if(line.startsWith("mtllib ")){
                    File mtlFile = VFS.getFileInSameFolder(objFile, currentLine[1]);
                    if(mtlFile.exists())
                        materialLibrary = new MaterialLibrary(mtlFile);
                }else if (line.startsWith("v ")) {
                    Vector3f vertex = new Vector3f(Float.valueOf(currentLine[1]),
                            Float.valueOf(currentLine[2]),
                            Float.valueOf(currentLine[3]));
                    VertexNM newVertex = new VertexNM(vertices.size(), vertex);
                    vertices.add(newVertex);

                } else if (line.startsWith("vt ")) {
                    Vector2f texture = new Vector2f(Float.valueOf(currentLine[1]),
                            Float.valueOf(currentLine[2]));
                    textures.add(texture);
                } else if (line.startsWith("vn ")) {
                    Vector3f normal = new Vector3f(Float.valueOf(currentLine[1]),
                            Float.valueOf(currentLine[2]),
                            Float.valueOf(currentLine[3]));
                    normals.add(normal);
                } else if (line.startsWith("f ")) {
                    break;
                }
            }
            int mtlID = 0;
            while (line != null) {
                String[] currentLine = line.split(" ");
                if(line.startsWith("f ")){
                    String[] vertex1 = currentLine[1].split("/");
                    String[] vertex2 = currentLine[2].split("/");
                    String[] vertex3 = currentLine[3].split("/");
                    VertexNM v0 = processVertex(vertex1, vertices, indices, mtlID);
                    VertexNM v1 = processVertex(vertex2, vertices, indices, mtlID);
                    VertexNM v2 = processVertex(vertex3, vertices, indices, mtlID);
                    calculateTangents(v0, v1, v2, textures);
                    for(int i=4;i<currentLine.length;++i){
                        vertex1 = currentLine[i-3].split("/");
                        vertex2 = currentLine[i-1].split("/");
                        vertex3 = currentLine[i].split("/");
                        v0 = processVertex(vertex1, vertices, indices, mtlID);
                        v1 = processVertex(vertex2, vertices, indices, mtlID);
                        v2 = processVertex(vertex3, vertices, indices, mtlID);
                        calculateTangents(v0, v1, v2, textures);
                    }
                }else if(line.startsWith("usemtl ")){
                    if(materialLibrary!=null)
                        mtlID = materialLibrary.getMaterialID(currentLine[1]);
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Error reading the file");
        }
        removeUnusedVertices(vertices);
        float[] verticesArray = new float[vertices.size() * 3];
        float[] texturesArray = new float[vertices.size() * 2];
        float[] normalsArray = new float[vertices.size() * 3];
        float[] tangentsArray = new float[vertices.size() * 3];
        int[] materialsArray = new int[vertices.size()];
        float furthest = convertDataToArrays(vertices, textures, normals, verticesArray,
                texturesArray, normalsArray, tangentsArray, materialsArray);
        int[] indicesArray = convertIndicesListToArray(indices);

        RawModel rawModel = Loader.loadToVAO(verticesArray, normalsArray, tangentsArray, texturesArray, indicesArray, materialsArray);
        TexturedModel texturedModel = new TexturedModel(rawModel);
        if(materialLibrary!=null)
            texturedModel.setMaterialLibrary(materialLibrary);
        else
            texturedModel.setMaterialLibrary(MaterialLibrary.createDefault());
        return texturedModel;
    }

    /**
     * Calculates the tangents for a given face
     *
     * @param v0 The first vertex
     * @param v1 The second vertex
     * @param v2 The third vertex
     * @param textures The array list of textures to use for uv coords
     */
    private static void calculateTangents(VertexNM v0, VertexNM v1, VertexNM v2,
                                          List<Vector2f> textures) {

        Vector3f deltaPos1 = v1.getPosition().sub(v0.getPosition(), new Vector3f());
        Vector3f deltaPos2 = v2.getPosition().sub(v0.getPosition(), new Vector3f());
        Vector2f uv0 = textures.get(v0.getTextureIndex());
        Vector2f uv1 = textures.get(v1.getTextureIndex());
        Vector2f uv2 = textures.get(v2.getTextureIndex());
        Vector2f deltaUv1 = uv1.sub(uv0, new Vector2f());
        Vector2f deltaUv2 = uv2.sub(uv0, new Vector2f());

        float r = 1.0f / (deltaUv1.x * deltaUv2.y - deltaUv1.y * deltaUv2.x);
        deltaPos1.mul(deltaUv2.y);
        deltaPos2.mul(deltaUv1.y);
        Vector3f tangent = deltaPos1.sub(deltaPos2, new Vector3f());
        tangent.mul(r);
        v0.addTangent(tangent);
        v1.addTangent(tangent);
        v2.addTangent(tangent);
    }

    /**
     * Processes a vertex and returns a vertexNM with all the data representing the vertex
     *
     * @param vertex The <code>String[]</code> of the vertex to process
     * @param vertices The list of vertices
     * @param indices The list of indices
     * @param mtlID The material of the vertex
     *
     * @return The normal mapped vertex with data about the vertex
     */
    private static VertexNM processVertex(String[] vertex, List<VertexNM> vertices,
                                          List<Integer> indices, int mtlID) {
        int index = Integer.parseInt(vertex[0]) - 1;
        VertexNM currentVertex = vertices.get(index);
        int textureIndex = Integer.parseInt(vertex[1]) - 1;
        int normalIndex = Integer.parseInt(vertex[2]) - 1;
        if (currentVertex.isUnset()) {
            currentVertex.setTextureIndex(textureIndex);
            currentVertex.setNormalIndex(normalIndex);
            currentVertex.setMaterial(mtlID);
            indices.add(index);
            return currentVertex;
        } else {
            return dealWithAlreadyProcessedVertex(currentVertex, textureIndex, normalIndex, indices,
                    vertices);
        }
    }

    /**
     * Converts the index list to an array
     *
     * @param indices The index list
     * @return an array of the indices
     */
    private static int[] convertIndicesListToArray(List<Integer> indices) {
        int[] indicesArray = new int[indices.size()];
        for (int i = 0; i < indicesArray.length; i++) {
            indicesArray[i] = indices.get(i);
        }
        return indicesArray;
    }

    /**
     * Processes all the data into their respective arrays
     *
     * @param vertices The vertex list
     * @param textures The texture list
     * @param normals The normal list
     * @param verticesArray The vertex array
     * @param texturesArray The texture array
     * @param normalsArray The normal array
     * @param tangentsArray The tangent array
     * @param materialsArray The material array
     * @return The furthest point from the origin
     */
    private static float convertDataToArrays(List<VertexNM> vertices, List<Vector2f> textures,
                                             List<Vector3f> normals, float[] verticesArray, float[] texturesArray,
                                             float[] normalsArray, float[] tangentsArray, int[] materialsArray) {
        float furthestPoint = 0;
        for (int i = 0; i < vertices.size(); i++) {
            VertexNM currentVertex = vertices.get(i);
            if (currentVertex.getLength() > furthestPoint) {
                furthestPoint = currentVertex.getLength();
            }
            Vector3f position = currentVertex.getPosition();
            Vector2f textureCoord = textures.get(currentVertex.getTextureIndex());
            Vector3f normalVector = normals.get(currentVertex.getNormalIndex());
            Vector3f tangent = currentVertex.getAverageTangent();
            verticesArray[i * 3] = position.x;
            verticesArray[i * 3 + 1] = position.y;
            verticesArray[i * 3 + 2] = position.z;
            texturesArray[i * 2] = textureCoord.x;
            texturesArray[i * 2 + 1] = 1 - textureCoord.y;
            normalsArray[i * 3] = normalVector.x;
            normalsArray[i * 3 + 1] = normalVector.y;
            normalsArray[i * 3 + 2] = normalVector.z;
            tangentsArray[i * 3] = tangent.x;
            tangentsArray[i * 3 + 1] = tangent.y;
            tangentsArray[i * 3 + 2] = tangent.z;
            materialsArray[i] = currentVertex.getMaterial();
        }
        return furthestPoint;
    }

    /**
     * Handles vertices to make sure there are no duplicate vertices
     *
     * @param previousVertex The vertex previously processed
     * @param newTextureIndex The new texture index of the vertex
     * @param newNormalIndex The new normal index of the vertex
     * @param indices The list of indices
     * @param vertices The list of vertices
     * @return The vertex of the duplicate vertex
     */
    private static VertexNM dealWithAlreadyProcessedVertex(VertexNM previousVertex, int newTextureIndex,
                                                           int newNormalIndex, List<Integer> indices, List<VertexNM> vertices) {
        if (previousVertex.hasSameTextureAndNormal(newTextureIndex, newNormalIndex)) {
            indices.add(previousVertex.getIndex());
            return previousVertex;
        } else {
            VertexNM anotherVertex = previousVertex.getDuplicateVertex();
            if (anotherVertex != null) {
                return dealWithAlreadyProcessedVertex(anotherVertex, newTextureIndex,
                        newNormalIndex, indices, vertices);
            } else {
                VertexNM duplicateVertex = previousVertex.duplicate(vertices.size());
                duplicateVertex.setTextureIndex(newTextureIndex);
                duplicateVertex.setNormalIndex(newNormalIndex);
                previousVertex.setDuplicateVertex(duplicateVertex);
                vertices.add(duplicateVertex);
                indices.add(duplicateVertex.getIndex());
                return duplicateVertex;
            }
        }
    }

    /**
     * Removes all vertices that arent used in faces
     *
     * @param vertices The list of vertices
     */
    private static void removeUnusedVertices(List<VertexNM> vertices) {
        for (VertexNM vertex : vertices) {
            vertex.averageTangents();
            if (vertex.isUnset()) {
                vertex.setTextureIndex(0);
                vertex.setNormalIndex(0);
            }
        }
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
                        TexturedModel texturedModel = loadModel(model);
                        assert texturedModel!=null;
                        while (index<parts.length){
                            String[] attribs = parts[index++].split(";",2);
                            switch (attribs[0]){
                                case "texture":
                                    texturedModel.setTexture(Texture.getTexture(attribs[1]));
                                    break;
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
