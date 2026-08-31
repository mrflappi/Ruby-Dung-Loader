package net.murfgames.rdloader.util.event;

public interface IEvent {
    void connect(Runnable listener);
    void disconnect(Runnable listener);
}
