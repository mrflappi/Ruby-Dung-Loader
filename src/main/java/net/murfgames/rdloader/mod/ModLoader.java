package net.murfgames.rdloader.mod;

import net.murfgames.rdloader.RubyDungLoader;
import net.murfgames.rdloader.mod.builtin.BuiltInMods;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public abstract class ModLoader {
    private static final String MODS_PATH = "mods/";
    private static final List<Mod> loadedMods = new ArrayList<>();

    public static Mod[] getMods() {
        return loadedMods.toArray(new Mod[0]);
    }

    public static JarMod[] getJarMods() {
        return loadedMods.stream()
            .filter(JarMod.class::isInstance)
            .map(JarMod.class::cast)
            .toArray(JarMod[]::new);
    }

    public static void loadMods() {
        RubyDungLoader.PRINTER.println("Loading Mods...");
        loadBuiltInMods();
        loadJarMods();
    }

    private static void loadBuiltInMods() {
        if (RubyDungLoader.DEBUG) loadedMods.add(BuiltInMods.DEBUG_MOD);
        loadedMods.add(BuiltInMods.ADVANCED_FEATURES_MOD);
        RubyDungLoader.PRINTER.println("Built-in mods loaded");
    }

    private static void loadJarMods() {
        RubyDungLoader.PRINTER.println("Loading jar mods...");

        // Identify or create mods folder
        File modsFolder = new File(System.getProperty("user.dir"), "/" + MODS_PATH);
        modsFolder.mkdir();

        // Get mods
        File[] mods = modsFolder.listFiles((dir, name) -> name.endsWith(".jar"));

        if (mods == null || mods.length == 0) {
            RubyDungLoader.PRINTER.println("No mods found!");
            return;
        }

        RubyDungLoader.PRINTER.println(mods.length + " mods found!");

        for (File modFile: mods) {
            try {
                Mod mod = loadJarMod(modFile);
                loadedMods.add(mod);
                RubyDungLoader.PRINTER.println("Mod loaded: " + mod.getId());
            } catch (Exception e) {
                RubyDungLoader.PRINTER.printerr("Failed to load mod " + modFile.getName(), e);
            }
        }
    }

    private static Mod loadJarMod(File modFile) throws Exception {
        // Get Jar
        JarFile modJar = new JarFile(modFile);
        Manifest manifest = modJar.getManifest();
        if (manifest == null) throw new Exception("No manifest found!");

        // Identify entry point
        String entryPointName = manifest.getMainAttributes().getValue("Entry-Point");
        if (entryPointName == null) throw new Exception("No entry point given!");;

        // Creates a ClassLoader which loads the class specified in manifest
        URLClassLoader modClassLoader = new URLClassLoader(
                new URL[]{modFile.toURI().toURL()},
                ModEntryPoint.class.getClassLoader());
        Class<?> modClass = modClassLoader.loadClass(entryPointName);

        if (!ModEntryPoint.class.isAssignableFrom(modClass))
            throw new Exception("Could not find entry point");

        // Define mod details
        String modId = manifest.getMainAttributes().getValue("Mod-Id");
        String modName = manifest.getMainAttributes().getValue("Mod-Name");
        String modVersion = manifest.getMainAttributes().getValue("Mod-Version");

        ModEntryPoint modInstance = (ModEntryPoint) modClass.getDeclaredConstructor().newInstance();

        return new JarMod(
                modId != null ? modId : modFile.getName(),
                modName != null ? modName : "Unknown",
                modVersion != null ? modVersion : "1.0",
                modInstance,
                modClassLoader,
                modJar
        );
    }
}
