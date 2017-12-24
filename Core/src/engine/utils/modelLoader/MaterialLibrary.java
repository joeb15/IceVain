package engine.utils.modelLoader;

import java.io.*;
import java.util.ArrayList;

public class MaterialLibrary {

    private ArrayList<Material> materials = new ArrayList<>();

    public MaterialLibrary(File materialFile){
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(materialFile));
            String line;
            Material currMaterial = null;

            while ((line=bufferedReader.readLine())!=null){
                String[] parts = line.split(" ");
                if(parts[0].equals("newmtl")){
                    if(currMaterial!=null)
                        materials.add(currMaterial);
                    currMaterial = new Material();
                    currMaterial.name = parts[1];
                }else if(currMaterial==null){
                }else if(parts[0].equals("Ns")){
                    currMaterial.specularExponent = Float.parseFloat(parts[1]);
                }else if(parts[0].equals("Ka")){
                    currMaterial.ambient.x = Float.parseFloat(parts[1]);
                    currMaterial.ambient.y = Float.parseFloat(parts[2]);
                    currMaterial.ambient.z = Float.parseFloat(parts[3]);
                }else if(parts[0].equals("Kd")){
                    currMaterial.diffuse.x = Float.parseFloat(parts[1]);
                    currMaterial.diffuse.y = Float.parseFloat(parts[2]);
                    currMaterial.diffuse.z = Float.parseFloat(parts[3]);
                }else if(parts[0].equals("Ks")){
                    currMaterial.specular.x = Float.parseFloat(parts[1]);
                    currMaterial.specular.y = Float.parseFloat(parts[2]);
                    currMaterial.specular.z = Float.parseFloat(parts[3]);
                }
            }
            if(currMaterial!=null)
                materials.add(currMaterial);
            for(Material m:materials){
                System.out.println(m.name);
                System.out.println(m.ambient);
                System.out.println(m.diffuse);
                System.out.println(m.specular);
                System.out.println(m.specularExponent);
                System.out.println();
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int indexOf(String material){
        for(int i=0;i<materials.size();i++){
            if(materials.get(i).name.equals(material))
                return i;
        }
        return 0;
    }

}
