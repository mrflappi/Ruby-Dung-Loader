package net.murfgames.rdloader.resource;

public abstract class Resource <T extends Object> {
    public final T data;

    public Resource(T data) {
        this.data = data;
    }
}
