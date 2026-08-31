package net.murfgames.rdloader.util.event;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

public class TerminalSignal<T> implements ITerminalSignal<T>{
    private final List<Predicate<T>> listeners = new ArrayList<>();
    private final BooleanSupplier condition;

    public TerminalSignal() {
        this.condition = () -> true;
    }

    public TerminalSignal(BooleanSupplier condition) {
        this.condition = condition;
    }

    public void connect(Predicate<T> listener) {
        listeners.add(listener);
    }

    public void disconnect(Predicate<T> listener) {
        listeners.remove(listener);
    }

    public void clear() {
        listeners.clear();
    }

    public boolean emit(T value) {
        if (!condition.getAsBoolean())
            return false;

        for (Predicate<T> listener : listeners) {
            if (listener.test(value)) {
                return true;
            }
        }

        return false;
    }
}
