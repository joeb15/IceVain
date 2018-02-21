package engine.utils.states;

public abstract class State {
    protected StateManager stateManager;
    public abstract void onCreate();

    public abstract void onDestroy();

}
