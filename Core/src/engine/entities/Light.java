package engine.entities;

import org.joml.Vector3f;

public class Light{
    private Vector3f position, color, attenuation;

    /**
     * Creates an instance of a light that can be passed into the render engine
     *
     * @param position The initial position of the light (x, y, z)
     * @param color The color of the light (r, g, b)
     * @param attenuation The attenuation factor of the light (a, bx, cx^2)
     */
    public Light(Vector3f position, Vector3f color, Vector3f attenuation){
        this.position=position;
        this.color=color;
        this.attenuation=attenuation;
    }

    /**
     * Getter for the color of the light
     *
     * @return The vector3f representation of the color
     */
    public Vector3f getColor() {
        return color;
    }

    /**
     * Getter for the attenuation factor of the light
     *
     * @return the vector3f associated with the attenuation factor of the light instance
     */
    public Vector3f getAttenuation() {
        return attenuation;
    }

    /**
     * Getter for the position of the light instance
     *
     * @return The vector3f representation of the light instance
     */
    public Vector3f getPosition() {
        return position;
    }
}
