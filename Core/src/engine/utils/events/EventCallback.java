package engine.utils.events;

/**
 * An interface to allow for event handling
 */
public interface EventCallback {
    /**
     * Called when an event is triggered
     *
     * @param event The Event object that contains data about the event
     */
    void onEvent(Event event);
}
