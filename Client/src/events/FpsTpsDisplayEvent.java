package events;

import engine.utils.events.Event;

public class FpsTpsDisplayEvent extends Event{

    public int fps, tps;

    public FpsTpsDisplayEvent(int fps, int tps) {
        this.fps = fps;
        this.tps = tps;
    }
}
