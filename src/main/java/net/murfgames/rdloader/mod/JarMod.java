package net.murfgames.rdloader.mod;

import java.util.jar.JarFile;

public final class JarMod extends Mod {
    private final ClassLoader classLoader;
    private final JarFile jarFile;

    public JarMod(String id, String name, String version, ModEntryPoint entryPoint, ClassLoader classLoader, JarFile jarFile) {
        super(id, name, version, entryPoint);
        this.classLoader = classLoader;
        this.jarFile = jarFile;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public JarFile getJarFile() {
        return jarFile;
    }
}
