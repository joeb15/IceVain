package engine.utils.states;

import engine.utils.events.Event;

public abstract class State {
    protected StateManager stateManager;
    public abstract void onCreate();
    public abstract void onEvent(Event e);
    public abstract void onDestroy();

}
