package engine.entities;

import engine.render.models.TexturedModel;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Entity {
    private TexturedModel texturedModel;
    private Vector3f pos, rot, scl;
    private Matrix4f transformationMatrix;

    public Entity(TexturedModel texturedModel, Vector3f pos, Vector3f rot, Vector3f scl) {
        this.pos = pos;
        this.rot = rot;
        this.scl = scl;
        this.texturedModel = texturedModel;
        recalculateTransformationMatrix();
    }

    private void recalculateTransformationMatrix() {
        transformationMatrix = new Matrix4f();
        transformationMatrix.translate(pos);
        transformationMatrix.rotate(rot.x, 1,0,0);
        transformationMatrix.rotate(rot.y, 0,1,0);
        transformationMatrix.rotate(rot.z, 0,0,1);
        transformationMatrix.scale(scl);
    }

    public void setPos(float x, float y, float z){
        pos.x=x;
        pos.y=y;
        pos.z=z;
        recalculateTransformationMatrix();
    }

    public void setRotation(float pitch, float roll, float yaw){
        rot.x=pitch;
        rot.y=roll;
        rot.z=yaw;
        recalculateTransformationMatrix();
    }

    public void move(float x, float y, float z){
        pos.add(x, y, z);
        recalculateTransformationMatrix();
    }

    public void rotate(float pitch, float roll, float yaw){
        rot.add(pitch, roll, yaw);
        recalculateTransformationMatrix();
    }

    public void scale(float x, float y, float z){
        scl.x=x;
        scl.y=y;
        scl.z=z;
        recalculateTransformationMatrix();
    }

    public Matrix4f getTransformationMatrix(){
        return transformationMatrix;
    }

    public TexturedModel getTexturedModel() {
        return texturedModel;
    }

    public Vector3f getPos() {
        return pos;
    }

    public Vector3f getRot() {
        return rot;
    }

    public Vector3f getScl() {
        return scl;
    }
}
