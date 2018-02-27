package engine.utils.states;

/**
 * Abstract class to assist with the game flow
 */
public abstract class State {
    protected StateManager stateManager;

    /**
     * Called upon the creation of the state
     */
    public abstract void onCreate();

    /**
     * Called upon the destruction of the state
     */
    public abstract void onDestroy();

}
