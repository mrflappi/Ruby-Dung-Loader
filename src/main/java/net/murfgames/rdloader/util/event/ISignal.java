package net.murfgames.rdloader.util.event;

import java.util.function.Consumer;

public interface ISignal<T> {
    void connect(Consumer<T> listener);
    void disconnect(Consumer<T> listener);
}
