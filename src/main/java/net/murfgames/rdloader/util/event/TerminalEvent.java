package net.murfgames.rdloader.util.event;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

public class TerminalEvent implements ITerminalEvent {
    private final List<BooleanSupplier> listeners = new ArrayList<>();
    private final BooleanSupplier condition;

    public TerminalEvent() {
        this.condition = () -> true;
    }

    public TerminalEvent(BooleanSupplier condition) {
        this.condition = condition;
    }

    public void connect(BooleanSupplier listener) {
        listeners.add(listener);
    }

    public void disconnect(BooleanSupplier listener) {
        listeners.remove(listener);
    }

    public void clear() {
        listeners.clear();
    }

    public boolean emit() {
        if (!condition.getAsBoolean())
            return false;

        for (BooleanSupplier listener : listeners) {
            if (listener.getAsBoolean()) {
                return true;
            }
        }

        return false;
    }
}
