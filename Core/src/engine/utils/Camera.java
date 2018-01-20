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

    private static final float degToRad = 0.017453293f;

    public Camera(){
        projection = new Matrix4f().perspective(70, 1280f/720f, 1f, 100);
        position = new Vector3f();
    }

    public void move(float x, float y, float z){
        position.add(x, y, z);
    }

    public Matrix4f getProjection() {
        return projection;
    }

    public Matrix4f getViewMatrix() {
        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.rotate(-pitch*degToRad,1,0,0);
        viewMatrix.rotate(-yaw*degToRad,0,1,0);
        viewMatrix.rotate(-roll*degToRad,0,0,1);
        viewMatrix.translate(position, viewMatrix);
        return viewMatrix;
    }

    public float getSpeed() {
        return speed;
    }

    public float getRotationSpeed() {
        return rotationSpeed;
    }

    public void rotate(float pitch, float yaw, float roll) {
        this.pitch+=pitch;
        this.yaw+=yaw;
        this.roll+=roll;
    }

    public void moveForward() {
        float amtX = (float) (Math.sin(yaw*degToRad) * Math.cos(pitch*degToRad));
        float amtZ = (float) (Math.cos(yaw*degToRad) * Math.cos(pitch*degToRad));
        float amtY = (float) Math.sin(pitch*degToRad);
        position.add(speed*amtX, speed*amtY, speed*amtZ);
    }

    public void moveBackward() {
        float amtX = (float) (Math.sin(yaw*degToRad) * Math.cos(pitch*degToRad));
        float amtZ = (float) (Math.cos(yaw*degToRad) * Math.cos(pitch*degToRad));
        float amtY = (float) Math.sin(pitch*degToRad);
        position.add(speed*-amtX, speed*-amtY, speed*-amtZ);
    }

    public void strafeLeft() {
        float amtX = (float) (Math.sin((yaw-90)*degToRad) * Math.cos(pitch*degToRad));
        float amtZ = (float) (Math.cos((yaw-90)*degToRad) * Math.cos(pitch*degToRad));
        float amtY = (float) Math.sin(pitch*degToRad);
        position.add(speed*-amtX, speed*-amtY, speed*-amtZ);
    }

    public void strafeRight() {
        float amtX = (float) (Math.sin((yaw-90)*degToRad) * Math.cos(pitch*degToRad));
        float amtZ = (float) (Math.cos((yaw-90)*degToRad) * Math.cos(pitch*degToRad));
        float amtY = (float) Math.sin(pitch*degToRad);
        position.add(speed*amtX, speed*amtY, speed*amtZ);
    }
}
