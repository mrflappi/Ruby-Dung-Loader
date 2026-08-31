package net.murfgames.rdloader.util;

import java.io.Serializable;
import java.util.Objects;

public class Identifier implements Serializable {
    private static final String DEFAULT_NAMESPACE = "minecraft";

    public final String namespace;
    public final String path;
    private final int hashCode;

    public Identifier(String namespace, String path) {
        this.namespace = namespace;
        this.path = path;
        this.hashCode = Objects.hash(namespace, path);
    }

    public Identifier(String path) {
        this.namespace = DEFAULT_NAMESPACE;
        this.path = path;
        this.hashCode = Objects.hash(namespace, path);
    }

    @Override
    public String toString() {
        return namespace + ":" + path;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass())
            return false;
        if (this == obj)
            return true;

        Identifier other = (Identifier) obj;
        return Objects.equals(other.namespace, this.namespace) && Objects.equals(other.path, this.path);
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }
}
