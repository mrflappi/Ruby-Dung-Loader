package net.murfgames.rdloader.util.event;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class ProtectedSignal<T> implements ISignal<T> {
    private final Signal<T> signal;

    public ProtectedSignal(ISignal<T> connectedSignal) {
        signal = new Signal<>();
        connectedSignal.connect(signal::emit);
    }

    public ProtectedSignal(ISignal<T> connectedSignal, BooleanSupplier condition) {
        signal = new Signal<>(condition);
        connectedSignal.connect(signal::emit);
    }

    public void connect(Consumer<T> listener) {
        signal.connect(listener);
    }

    public void disconnect(Consumer<T> listener) {
        signal.disconnect(listener);
    }
}
