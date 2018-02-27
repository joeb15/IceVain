package events;

import engine.utils.events.Event;

public class FpsTpsDisplayEvent extends Event{

    public float fps;
    public float tps;

    /**
     * The event that is created in order to update the game about the current fps and tps
     * @param fps the current frames per second
     * @param tps the curremt ticks per second
     */
    public FpsTpsDisplayEvent(float fps, float tps) {
        this.fps = fps;
        this.tps = tps;
    }
}
