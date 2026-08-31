package net.murfgames.rdloader.util.event;

import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

public class ProtectedTerminalEvent implements ITerminalEvent {
    private final TerminalEvent event;

    public ProtectedTerminalEvent(ITerminalEvent connectedEvent) {
        event = new TerminalEvent();
        connectedEvent.connect(event::emit);
    }

    public ProtectedTerminalEvent(ITerminalEvent connectedEvent, BooleanSupplier condition) {
        event = new TerminalEvent(condition);
        connectedEvent.connect(event::emit);
    }

    public void connect(BooleanSupplier listener) {
        event.connect(listener);
    }

    public void disconnect(BooleanSupplier listener) {
        event.disconnect(listener);
    }
}
