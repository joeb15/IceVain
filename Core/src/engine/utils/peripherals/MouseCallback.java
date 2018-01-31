package engine.utils.peripherals;

import org.joml.Vector2f;

public interface MouseCallback {
    void mouseEvent(boolean[] pressed, boolean[] wasPressed, Vector2f lastPos, Vector2f pos, Vector2f[] lastDown);
}
