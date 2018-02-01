package engine.utils.states;

public class StateManager {

    private State currentState;

    public StateManager(State initialState){
        setState(initialState);
    }

    public void setState(State newState){
        if(currentState!=null)
            currentState.onDestroy();
        currentState=newState;
        currentState.stateManager=this;
        currentState.onCreate();
    }

}
