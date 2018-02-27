package engine.utils.states;

public class StateManager {

    private State currentState;

    /**
     * Creates a state manager with an initial state to display
     *
     * @param initialState The initital state to start the game in
     */
    public StateManager(State initialState){
        setState(initialState);
    }

    /**
     * Changes the state of the game, destroying the previous state
     *
     * @param newState The new state of the game
     */
    public void setState(State newState){
        if(currentState!=null)
            currentState.onDestroy();
        currentState=newState;
        currentState.stateManager=this;
        currentState.onCreate();
    }

}
