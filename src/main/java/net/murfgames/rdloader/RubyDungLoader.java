package net.murfgames.rdloader;

import net.murfgames.rdloader.agent.wrapper.RubyDungWrapper;
import net.murfgames.rdloader.audio.AudioMaster;
import net.murfgames.rdloader.gamemode.GameMode;
import net.murfgames.rdloader.mod.Mod;
import net.murfgames.rdloader.mod.ModLoader;
import net.murfgames.rdloader.mod.ModPrinter;
import net.murfgames.rdloader.resource.ResourceManager;
import net.murfgames.rdloader.util.event.Event;
import net.murfgames.rdloader.util.event.ProtectedEvent;

public abstract class RubyDungLoader {
    public static final ModPrinter PRINTER = new ModPrinter("Ruby Dung Loader");
    public static final boolean DEBUG = true;

    public static boolean initialised = false;

    private static final Event INITIALISE = new Event();
    public static final ProtectedEvent INITIALISE_EVENT = new ProtectedEvent(INITIALISE);

    private static final Event PRE_TICK = new Event();
    public static final ProtectedEvent PRE_TICK_EVENT = new ProtectedEvent(PRE_TICK);

    private static final Event POST_TICK = new Event();
    public static final ProtectedEvent POST_TICK_EVENT = new ProtectedEvent(POST_TICK);

    public static void init(Object rubyDungInstance, ProtectedEvent initialiseEvent) {
        AudioMaster.init();
        AudioMaster.setListenerData();
        initialiseEvent.connect(RubyDungLoader::onInitialise);

        try {
            RubyDungWrapper.accessRubyDung(rubyDungInstance);
        } catch (NoSuchFieldException e) {
            PRINTER.printerr("Failed to access RubyDung", e);
        }

        try {
            ModLoader.loadMods();
        } catch (Exception e) {
            PRINTER.printerr("Failed to load mods", e);
        }

        try {
            ResourceManager.loadResources();
        } catch (Exception e) {
            PRINTER.printerr("Failed to load resources", e);
        }

        initialiseMods();
    }

    private static void onInitialise() {
        initialised = true;
        INITIALISE.emit();
    }

    public static void initialiseMods() {
        for (Mod mod: ModLoader.getMods()) {
            try {
                PRINTER.println(String.format("Initialising %s...", mod.getId()));
                mod.getEntryPoint().onInitialise();
            } catch (Exception e) {
                PRINTER.printerr(String.format("Failed to access %s", mod.getId()), e);
            }
        }
    }

    public static void preTick() {
        PRE_TICK.emit();
        KeyboardEventHandler.onTick();
        RubyDungLoader.tickMods();
    }

    public static void postTick() {
        POST_TICK.emit();
    }

    private static void tickMods() {
        for (Mod mod: ModLoader.getMods()) {
            mod.getEntryPoint().onTick();
        }
    }
}
