package engine.entities;

import engine.render.models.RawModel;
import engine.render.models.TexturedModel;
import engine.render.textures.Texture;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Entity {
    private TexturedModel texturedModel;
    private Vector3f pos, rot, scl;
    private Matrix4f transformationMatrix;

    /**
     * Creates an instance to represent an entity within the rendering engine
     *
     * @param texturedModel The textured model that is to be drawn to the screen
     * @param pos The position of the entity as it is initialized to
     * @param rot The initial rotation of the entity instance
     * @param scl The initial scale of the entity instance
     */
    public Entity(TexturedModel texturedModel, Vector3f pos, Vector3f rot, Vector3f scl) {
        this.pos = pos;
        this.rot = rot;
        this.scl = scl;
        this.texturedModel = texturedModel;
        recalculateTransformationMatrix();
    }

    /**
     * Creates an instance of an entity with a rawmodel and texture
     *
     * @param rawModel The raw model to associate the entity with
     * @param texture The texture to be given to the entity
     * @param pos The position of the entity as it is initialized to
     * @param rot The initial rotation of the entity instance
     * @param scl The initial scale of the entity instance
     */
    public Entity(RawModel rawModel, Texture texture, Vector3f pos, Vector3f rot, Vector3f scl){
        this(new TexturedModel(rawModel, texture), pos, rot, scl);
    }

    /**
     * Calculates the transformation matrix when the entity is moved or rotated.
     */
    private void recalculateTransformationMatrix() {
        transformationMatrix = new Matrix4f();
        transformationMatrix.translate(pos);
        transformationMatrix.rotate(rot.x, 1,0,0);
        transformationMatrix.rotate(rot.y, 0,1,0);
        transformationMatrix.rotate(rot.z, 0,0,1);
        transformationMatrix.scale(scl);
    }

    /**
     * Sets the position of an entity
     *
     * @param x The new x-coordinate of the entity
     * @param y The new y-coordinate of the entity
     * @param z The new z-coordinate of the entity
     */
    public void setPos(float x, float y, float z){
        pos.x=x;
        pos.y=y;
        pos.z=z;
        recalculateTransformationMatrix();
    }

    /**
     * Sets the rotation of an entity
     *
     * @param pitch The new pitch of the entity
     * @param roll the new roll of the entity
     * @param yaw The new yaw of the entity
     */
    public void setRotation(float pitch, float roll, float yaw){
        rot.x=pitch;
        rot.y=roll;
        rot.z=yaw;
        recalculateTransformationMatrix();
    }

    /**
     * Moves the entity by a certain amount
     *
     * @param x The delta x to move the entity by
     * @param y The delta y to move the entity by
     * @param z The delta z to move the entity by
     */
    public void move(float x, float y, float z){
        pos.add(x, y, z);
        recalculateTransformationMatrix();
    }

    /**
     * Rotates the entity by a certain amount
     *
     * @param pitch The delta pitch to rotate the entity by
     * @param roll The delta roll to rotate the entity by
     * @param yaw The delta yaw to rotate the entity by
     */
    public void rotate(float pitch, float roll, float yaw){
        rot.add(pitch, roll, yaw);
        recalculateTransformationMatrix();
    }

    /**
     * Sets the scale of the current entity to the given values
     *
     * @param x The x scale of the entity
     * @param y The y scale of the entity
     * @param z The z scale of the entity
     */
    public void scale(float x, float y, float z){
        scl.x=x;
        scl.y=y;
        scl.z=z;
        recalculateTransformationMatrix();
    }

    /**
     * Getter for the transformation matrix of the entity
     *
     * @return The transformation matrix of the entity
     */
    public Matrix4f getTransformationMatrix(){
        return transformationMatrix;
    }

    /**
     * Getter for the textured model of the entity
     *
     * @return The textured model that is associated with the entity
     */
    public TexturedModel getTexturedModel() {
        return texturedModel;
    }

    /**
     * Getter for the current position of the entity
     *
     * @return The position of the entity
     */
    public Vector3f getPos() {
        return pos;
    }

    /**
     * Getter for the current rotation of the entity
     *
     * @return the current rotation of the entity
     */
    public Vector3f getRot() {
        return rot;
    }

    /**
     * Gets the x y and z scake if the current entity
     *
     * @return The given scale for the entity
     */
    public Vector3f getScl() {
        return scl;
    }
}
