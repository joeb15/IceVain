package engine.utils.peripherals;

import org.joml.Vector2f;

/**
 * An interface to help with mouse callbacks
 */
public interface MouseCallback {
    /**
     * Triggered every tick to call events
     *
     * @param pressed boolean array representing pressed buttons
     * @param wasPressed boolean array representing previously pressed buttons
     * @param lastPos the last position of the mouse
     * @param pos the current position of the mouse
     * @param lastDown the last position a mouse was down with each button
     */
    void mouseEvent(boolean[] pressed, boolean[] wasPressed, Vector2f lastPos, Vector2f pos, Vector2f[] lastDown);
}
