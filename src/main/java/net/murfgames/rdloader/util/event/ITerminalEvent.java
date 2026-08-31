package net.murfgames.rdloader.util.event;

import java.util.function.BooleanSupplier;

public interface ITerminalEvent {
    void connect(BooleanSupplier listener);
    void disconnect(BooleanSupplier listener);
}
