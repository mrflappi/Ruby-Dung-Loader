package net.murfgames.rdloader.resource;

import javafx.util.Pair;
import net.murfgames.rdloader.RubyDungLoader;
import net.murfgames.rdloader.mod.JarMod;
import net.murfgames.rdloader.mod.Mod;
import net.murfgames.rdloader.mod.ModLoader;
import net.murfgames.rdloader.util.Identifier;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.*;
import java.util.jar.JarFile;

public class ResourceManager {
    public static final String RESOURCE_PACKS_PATH = "resourcepacks/";
    public static final String ASSETS_PATH = "assets/";

    private static final Map<Identifier, Resource<?>> loadedAssets = new HashMap<>();

    public static void loadResources() {
        RubyDungLoader.PRINTER.println("Loading resources...");
        loadVanillaResources();

        for (JarMod mod: ModLoader.getJarMods())
            loadJarModResources(mod);

        loadResourcePacks();
    }

    // Reshuffle vanilla Minecraft resources to fit resource pack layout
    private static void loadVanillaResources() {
        final String[] resources = {"terrain.png", "char.png"};

        for (String r: resources) {
            try {
                BufferedImage img = ImageIO.read(Objects.requireNonNull(ResourceManager.class.getResourceAsStream("/" + r)));
                loadedAssets.put(new Identifier("minecraft", r), new ImageResource(img));
            } catch (Exception e) {
                RubyDungLoader.PRINTER.printerr("Failed to load resource " + r, e);
            }
        }
    }

    private static void loadJarModResources(JarMod mod) {
        RubyDungLoader.PRINTER.println("Loading resources from mod " + mod.getId());
        try {
            JarFile jar = mod.getJarFile();
            loadJarResources(jar);
            RubyDungLoader.PRINTER.println("Loaded mod resources from mod " + mod.getId());
        } catch (Exception e) {
            RubyDungLoader.PRINTER.printerr("Failed to load mod resources from mod " + mod.getId(), e);
        }
    }

    // Load resources from mod Jar
    private static void loadJarResources(JarFile jar) {
        jar.stream()
                .filter(entry -> !entry.isDirectory() && entry.getName().startsWith("run/resourcepacks/rdloader/assets/"))
                .forEach(entry -> {
                    try (InputStream in = jar.getInputStream(entry)) {
                        String fullPath = entry.getName().substring("run/resourcepacks/rdloader/assets/".length());
                        String[] parts = fullPath.split("/", 2);
                        if (parts.length < 2) throw new Exception("Resource path invalid");

                        String namespace = parts[0];
                        String path = parts[1];

                        Optional<Pair<String, Resource<?>>> opAsset = loadResource(in, path);

                        opAsset.ifPresent(asset -> {
                            loadedAssets.put(new Identifier(namespace, asset.getKey()), asset.getValue());
                        });

                    } catch (Exception e) {
                        RubyDungLoader.PRINTER.printerr("Failed to load resource " + entry.getName(), e);
                    }
                });
    }

    // Load all available resource packs
    private static void loadResourcePacks() {
        File resourcePacksFolder = new File((System.getProperty("user.dir")) + "/" + RESOURCE_PACKS_PATH);
        resourcePacksFolder.mkdir();
        File[] resourcePacks = resourcePacksFolder.listFiles(File::isDirectory);

        if (resourcePacks == null || resourcePacks.length == 0) {
            RubyDungLoader.PRINTER.println("No resourcepacks found!");
            return;
        }

        RubyDungLoader.PRINTER.println(resourcePacks.length + " resource packs found!");

        for (File pack: resourcePacks) {
            try {
                loadResourcePack(pack);
            } catch (Exception e) {
                RubyDungLoader.PRINTER.printerr("Failed to load resourcepack: " + pack.getName(), e);
            }
        }
    }

    // Load resources from specific resource pack
    private static void loadResourcePack(File pack) throws Exception {
        File assetsFolder = new File(pack, ASSETS_PATH);
        assetsFolder.mkdir();
        File[] namespaces = assetsFolder.listFiles(File::isDirectory);
        if (namespaces == null || namespaces.length == 0) throw new Exception("No namespaces found!");

        for (File namespace: namespaces) {
            List<Pair<String, Resource<?>>> assets = loadResourcesFromFolder(namespace);

            for (Pair<String, Resource<?>> asset: assets)
                loadedAssets.put(new Identifier(namespace.getName(), asset.getKey()), asset.getValue());
        }

        RubyDungLoader.PRINTER.println("Resource pack loaded: " + pack.getName());
    }

    private static List<Pair<String, Resource<?>>> loadResourcesFromFolder(File folder) {
        return loadResourcesFromFolder(folder, "");
    }

    private static List<Pair<String, Resource<?>>> loadResourcesFromFolder(File folder, String pathPrefix) {
        List<Pair<String, Resource<?>>> resources = new ArrayList<>();
        for (File file : Objects.requireNonNull(folder.listFiles())) {
            if (file.isDirectory()) {
                resources.addAll(loadResourcesFromFolder(file, pathPrefix + file.getName() + "/"));
            } else {
                String assetKey = pathPrefix + file.getName();
                try {
                    Optional<Pair<String, Resource<?>>> resource = loadResource(file, pathPrefix + file.getName());
                    resource.ifPresent(resources::add);
                } catch (Exception e) {
                    RubyDungLoader.PRINTER.printerr("Failed to load asset " + assetKey, e);
                }
            }
        }

        return resources;
    }

    private static Optional<Pair<String, Resource<?>>> loadResource(InputStream in, String path) throws Exception {
        String fileSuffix = getFileSuffix(path);
        if (Arrays.asList(ImageIO.getReaderFileSuffixes()).contains(fileSuffix)) {
            return Optional.of(new Pair<>(path, ImageResource.load(in)));
        } else if (fileSuffix.equals("wav")) {
            return Optional.of(new Pair<>(path, AudioResource.load(in)));
        }

        return Optional.empty();
    }

    private static Optional<Pair<String, Resource<?>>> loadResource(File in, String path) throws Exception {
        String fileSuffix = getFileSuffix(path);
        if (Arrays.asList(ImageIO.getReaderFileSuffixes()).contains(fileSuffix)) {
            return Optional.of(new Pair<>(path, ImageResource.load(in)));
        } else if (fileSuffix.equals("wav")) {
            return Optional.of(new Pair<>(path, AudioResource.load(in)));
        }

        return Optional.empty();
    }

    private static String getFileSuffix(String path) throws Exception {
        int suffixStart = path.lastIndexOf('.');
        if (suffixStart < 0 || suffixStart >= path.length() - 1)
            throw new Exception("Resource path invalid");

        return path.substring(suffixStart + 1);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Resource<?>> Optional<T> getAsset(Identifier id, Class<T> resourceType) {
        if (id.path.charAt(0) == '/')
            id = new Identifier(id.namespace, id.path.substring(1));

        if (loadedAssets.containsKey(id)) {
            Resource<?> asset = loadedAssets.get(id);
            if (asset.getClass() == resourceType)
                return Optional.of((T) asset);
        }

        return Optional.empty();
    }

    public static <T extends Resource<?>> Optional<T> getAsset(String id, Class<T> resourceType) {
        return getAsset(new Identifier(id), resourceType);
    }
}
