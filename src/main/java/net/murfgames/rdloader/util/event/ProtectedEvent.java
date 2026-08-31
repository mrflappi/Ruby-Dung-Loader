package net.murfgames.rdloader.util.event;

import java.util.function.BooleanSupplier;

public class ProtectedEvent implements IEvent {
    private final Event event;

    public ProtectedEvent(IEvent connectedEvent) {
        event = new Event();
        connectedEvent.connect(event::emit);
    }

    public ProtectedEvent(IEvent connectedEvent, BooleanSupplier condition) {
        event = new Event(condition);
        connectedEvent.connect(event::emit);
    }

    public void connect(Runnable listener) {
        event.connect(listener);
    }

    public void disconnect(Runnable listener) {
        event.disconnect(listener);
    }
}
