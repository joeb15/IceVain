package engine.entities;

import org.joml.Vector3f;

public class Light{
    private Vector3f position, color, attenuation;

    public Light(Vector3f position, Vector3f color, Vector3f attenuation){
        this.position=position;
        this.color=color;
        this.attenuation=attenuation;
    }

    public Vector3f getColor() {
        return color;
    }

    public Vector3f getAttenuation() {
        return attenuation;
    }

    public Vector3f getPosition() {
        return position;
    }
}
