package engine.utils.events;

import java.util.ArrayList;
import java.util.HashMap;

public class EventHandler {

    private static HashMap<String, ArrayList<EventCallback>> eventCallbacks = new HashMap<>();

    public static void addEventCallback(String eventName, EventCallback eventCallback){
        if(!eventCallbacks.containsKey(eventName))
            eventCallbacks.put(eventName, new ArrayList<>());
        eventCallbacks.get(eventName).add(eventCallback);
    }

    public static void onEvent(String eventName, Event event){
        if(eventCallbacks.containsKey(eventName))
            for(EventCallback eventCallback:eventCallbacks.get(eventName))
                eventCallback.onEvent(event);
    }

    public static void onEvent(String eventName) {
        onEvent(eventName, new Event() {});
    }
}
