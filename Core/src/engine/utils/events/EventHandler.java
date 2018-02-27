package engine.utils.events;

import java.util.ArrayList;
import java.util.HashMap;

public class EventHandler {

    private static HashMap<String, ArrayList<EventCallback>> eventCallbacks = new HashMap<>();

    /**
     * Adds an event callback that handles an event
     *
     * @param eventName The name of the event being triggered
     * @param eventCallback The callback that is called when the event is triggered
     */
    public static void addEventCallback(String eventName, EventCallback eventCallback){
        if(!eventCallbacks.containsKey(eventName))
            eventCallbacks.put(eventName, new ArrayList<>());
        eventCallbacks.get(eventName).add(eventCallback);
    }

    /**
     * Called when triggering an event
     *
     * @param eventName The name of the event being triggered
     * @param event The event object containing the data about the event
     */
    public static void onEvent(String eventName, Event event){
        if(eventCallbacks.containsKey(eventName))
            for(EventCallback eventCallback:eventCallbacks.get(eventName))
                eventCallback.onEvent(event);
    }

    /**
     * Called when triggering an event
     *
     * @param eventName The name of the event being triggered
     */
    public static void onEvent(String eventName) {
        onEvent(eventName, new Event() {});
    }
}
