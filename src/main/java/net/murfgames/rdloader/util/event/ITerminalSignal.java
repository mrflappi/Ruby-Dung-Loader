package net.murfgames.rdloader.util.event;

import java.util.function.Predicate;

public interface ITerminalSignal <T> {
    void connect(Predicate<T> listener);
    void disconnect(Predicate<T> listener);
}
