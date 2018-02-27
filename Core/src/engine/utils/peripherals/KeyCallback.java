package engine.utils.peripherals;

/**
 * Interface to help with key callbacks
 */
public interface KeyCallback {
    /**
     * Callback that is called upon each frame
     *
     * @param pressed Whether or not the key is pressed this tick
     * @param wasPressed Whether or not the key was pressed last tick
     */
    void keyEvent(boolean pressed, boolean wasPressed);
}
