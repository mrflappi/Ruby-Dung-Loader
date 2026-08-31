package net.murfgames.rdloader.mod;

import java.util.jar.JarFile;

public class Mod {
    private final String id;
    private final String name;
    private final String version;
    private final ModEntryPoint entryPoint;

    public Mod(String id, String name, String version, ModEntryPoint entryPoint) {
        this.id = id;
        this.name = name;
        this.version = version;
        this.entryPoint = entryPoint;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public ModEntryPoint getEntryPoint() {
        return entryPoint;
    }
}
