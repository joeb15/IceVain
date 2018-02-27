package engine.utils;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {

    private Matrix4f projection;
    private Vector3f position;

    private float speed = 0.1f;
    private float rotationSpeed = 2f;
    private float pitch = 0f;
    private float yaw = 0f;
    private float roll = 0f;
    private Vector3f negatedPos = new Vector3f();
    private static final float degToRad = 0.017453293f;

    /**
     * Creates a camera instance to keep track of the players view
     */
    public Camera(){
        projection = new Matrix4f().perspective(70, 1280f/720f, 1f, 1000f);
        position = new Vector3f();
    }

    /**
     * Moves the camera by a certain amount
     * @param x the x amount to move the camera by
     * @param y the y amount to move the camera by
     * @param z the z amount to move the camera by
     */
    public void move(float x, float y, float z){
        position.add(x, y, z);
    }

    /**
     * Getter for the projection matrix
     *
     * @return The projection matrix associated with the camera
     */
    public Matrix4f getProjection() {
        return projection;
    }

    /**
     * Getter for the view matrix
     *
     * @return The view matrix associated with the camera
     */
    public Matrix4f getViewMatrix() {
        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.rotate(-pitch*degToRad,1,0,0);
        viewMatrix.rotate(-yaw*degToRad,0,1,0);
        viewMatrix.rotate(-roll*degToRad,0,0,1);
        position.negate(negatedPos);
        viewMatrix.translate(negatedPos, viewMatrix);
        return viewMatrix;
    }

    /**
     * Getter for the speed of the camera
     *
     * @return The camera's maximum speed
     */
    public float getSpeed() {
        return speed;
    }

    /**
     * Getter for the rotational speed of the camera
     *
     * @return The camera's maximum rotational speed
     */
    public float getRotationSpeed() {
        return rotationSpeed;
    }

    /**
     * Rotates the camera
     *
     * @param pitch The amount to rotate the pitch by
     * @param yaw The amount to rotate the yaw by
     * @param roll The amount to rotate the roll by
     */
    public void rotate(float pitch, float yaw, float roll) {
        this.pitch+=pitch;
        this.yaw+=yaw;
        this.roll+=roll;
    }

    /**
     * Moves the player forward
     */
    public void moveForward() {
        float amtX = (float) -(Math.sin(yaw*degToRad) * Math.cos(pitch*degToRad));
        float amtZ = (float) -(Math.cos(yaw*degToRad) * Math.cos(pitch*degToRad));
        float amtY = (float) Math.sin(pitch*degToRad);
        position.add(speed*amtX, speed*amtY, speed*amtZ);
    }

    /**
     * Moves the player backward
     */
    public void moveBackward() {
        float amtX = (float) (Math.sin(yaw*degToRad) * Math.cos(pitch*degToRad));
        float amtZ = (float) (Math.cos(yaw*degToRad) * Math.cos(pitch*degToRad));
        float amtY = (float) -Math.sin(pitch*degToRad);
        position.add(speed*amtX, speed*amtY, speed*amtZ);
    }

    /**
     * Moves the player left
     */
    public void strafeLeft() {
        float amtX = (float) (Math.sin((yaw-90)*degToRad) * Math.cos(pitch*degToRad));
        float amtZ = (float) (Math.cos((yaw-90)*degToRad) * Math.cos(pitch*degToRad));
        float amtY = (float) -Math.sin(pitch*degToRad);
        position.add(speed*amtX, speed*amtY, speed*amtZ);
    }

    /**
     * Moves the player right
     */
    public void strafeRight() {
        float amtX = (float) (Math.sin((yaw+90)*degToRad) * Math.cos(pitch*degToRad));
        float amtZ = (float) (Math.cos((yaw+90)*degToRad) * Math.cos(pitch*degToRad));
        float amtY = (float) Math.sin(pitch*degToRad);
        position.add(speed*amtX, speed*amtY, speed*amtZ);
    }
}
