package net.murfgames.rdloader.util.event;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class Signal<T> implements ISignal<T>{
    private final List<Consumer<T>> listeners = new ArrayList<>();
    private final BooleanSupplier condition;

    public Signal() {
        this.condition = () -> true;
    }

    public Signal(BooleanSupplier condition) {
        this.condition = condition;
    }

    public void connect(Consumer<T> listener) {
        listeners.add(listener);
    }

    public void disconnect(Consumer<T> listener) {
        listeners.remove(listener);
    }

    public void clear() {
        listeners.clear();
    }

    public void emit(T value) {
        if (condition.getAsBoolean())
            listeners.forEach(listener -> listener.accept(value));
    }
}
