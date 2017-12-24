package engine.utils.modelLoader;

import engine.render.MaterialModel;
import org.joml.Vector3f;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class OBJLoader {

    public static MaterialModelGroup loadOBJ(File objFile){
        HashMap<String, MaterialModel> materialModels = new HashMap<>();
        MaterialLibrary materialLibrary = null;
        String parentFolder = objFile.getParent()+"/";
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(objFile));
            String line;
            int currMaterial = 0;
            HashMap<String, OBJObject> objObjects = new HashMap<>();
            OBJObject currObject = null;
            while ((line=bufferedReader.readLine())!=null){
                String[] parts = line.split(" ");
                if(parts[0].equals("mtllib")){
                    materialLibrary = new MaterialLibrary(new File(parentFolder+parts[1]));
                }else if(parts[0].equals("o")){
                    if(currObject!=null)
                        objObjects.put(currObject.name, currObject);
                    currObject = new OBJObject();
                    currObject.name = parts[1];
                }else if(parts[0].equals("v")){
                    Vector3f vertex = new Vector3f();
                    vertex.x = Float.parseFloat(parts[1]);
                    vertex.y = Float.parseFloat(parts[2]);
                    vertex.z = Float.parseFloat(parts[3]);
                    currObject.vertices.add(vertex);
                }else if(parts[0].equals("usemtl")){
                    currMaterial = materialLibrary.indexOf(parts[1]);
                }else if(parts[0].equals("f")){
                    for(int i=1;i<4;i++){
                        String[] faceParts = parts[i].split("/");
                        currObject.indicies.add(Integer.parseInt(faceParts[0])-1);
                        currObject.materials.add(currMaterial);
                    }
                }
            }

            for(OBJObject objObject:objObjects.values()){
                float[] vertices = new float[objObject.vertices.size()*3];
                int[] indices = new int[objObject.indicies.size()];
                int[] materials = new int[objObject.indicies.size()];
                for(int i=0;i<vertices.length/3;i++) {
                    vertices[i * 3 + 0] = objObject.vertices.get(i).x;
                    vertices[i * 3 + 1] = objObject.vertices.get(i).y;
                    vertices[i * 3 + 2] = objObject.vertices.get(i).z;
                }
                for(int i=0;i<indices.length;i++){
                    indices[i] = objObject.indicies.get(i);
                    materials[i] = objObject.materials.get(i);
                }
                MaterialModel materialModel = new MaterialModel(vertices, materials, indices);
                materialModels.put(objObject.name, materialModel);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new MaterialModelGroup(materialModels, materialLibrary);
    }

}
