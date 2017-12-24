package engine.utils.modelLoader;

import engine.render.MaterialModel;

import java.util.HashMap;

public class MaterialModelGroup {
    private static HashMap<String, MaterialModel> materialModels;
    private static MaterialLibrary materialLibrary;

    public MaterialModelGroup(HashMap<String, MaterialModel> materialModels, MaterialLibrary materialLibrary) {
        this.materialModels=materialModels;
        this.materialLibrary=materialLibrary;
    }

    public void render(String partName){
        MaterialModel model = materialModels.get(partName);
        if(model==null)return;
        model.bind();
        model.render();
        model.unbind();
    }

    public void render(){
        for(MaterialModel materialModel:materialModels.values()){
            materialModel.bind();
            materialModel.render();
            materialModel.unbind();
        }
    }
}
