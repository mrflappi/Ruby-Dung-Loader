package net.murfgames.rdloader.util.event;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

public class Event implements IEvent {
    private final List<Runnable> listeners = new ArrayList<>();
    private final BooleanSupplier condition;

    public Event() {
        this.condition = () -> true;
    }

    public Event(BooleanSupplier condition) {
        this.condition = condition;
    }

    public void connect(Runnable listener) {
        listeners.add(listener);
    }

    public void disconnect(Runnable listener) {
        listeners.remove(listener);
    }

    public void clear() {
        listeners.clear();
    }

    public void emit() {
        if (condition.getAsBoolean())
            listeners.forEach(Runnable::run);
    }
}
