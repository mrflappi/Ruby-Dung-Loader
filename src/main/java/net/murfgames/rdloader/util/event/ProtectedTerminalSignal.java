package net.murfgames.rdloader.util.event;

import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

public class ProtectedTerminalSignal<T> implements ITerminalSignal<T> {
    private final TerminalSignal<T> signal;

    public ProtectedTerminalSignal(ITerminalSignal<T> connectedSignal) {
        signal = new TerminalSignal<>();
        connectedSignal.connect(signal::emit);
    }

    public ProtectedTerminalSignal(ITerminalSignal<T> connectedSignal, BooleanSupplier condition) {
        signal = new TerminalSignal<>(condition);
        connectedSignal.connect(signal::emit);
    }

    public void connect(Predicate<T> listener) {
        signal.connect(listener);
    }

    public void disconnect(Predicate<T> listener) {
        signal.disconnect(listener);
    }
}
