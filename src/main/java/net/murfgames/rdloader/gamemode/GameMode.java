package net.murfgames.rdloader.gamemode;

import net.murfgames.rdloader.RubyDungLoader;
import net.murfgames.rdloader.util.Identifier;
import net.murfgames.rdloader.util.event.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class GameMode {
    private static GameMode currentGameMode;
    private static final Map<Identifier, GameMode> gameModes = new HashMap<Identifier, GameMode>();

    public final Identifier identifier;

    public GameMode(Identifier identifier) {
        this.identifier = identifier;
    }

    public static void registerGameMode(GameMode gameMode) {
        ProtectedEvent POST_TICK = new ProtectedEvent(RubyDungLoader.POST_TICK_EVENT, gameMode::isActive);
        POST_TICK.connect(gameMode::onTick);

        gameModes.put(gameMode.identifier, gameMode);

        if (currentGameMode == null)
            currentGameMode = gameMode;
    }

    public static GameMode getGameMode() {
        return currentGameMode;
    }

    public static <T extends GameMode> T getGameMode(Class<T> type) {
        if (type.isInstance(currentGameMode)) {
            return type.cast(currentGameMode);
        }
        throw new IllegalArgumentException("Game mode " + currentGameMode + " is not a " + type.getSimpleName());
    }

    public static void setGameMode(GameMode gameMode) {
        setGameMode(gameMode.identifier);
    }

    public static void setGameMode(Identifier id) {
        if (!gameModes.containsKey(id)) RubyDungLoader.PRINTER.printerr("Unknown GameMode " + id);
        GameMode gameMode = gameModes.get(id);

        currentGameMode.onExit();
        currentGameMode = gameMode;
        currentGameMode.onEnter();
    }

    public void connectEvent(IEvent caller, Runnable listener) {
        ProtectedEvent protectedEvent = new ProtectedEvent(caller, this::isActive);
        protectedEvent.connect(listener);
    }

    public void connectTerminalEvent(ITerminalEvent caller, BooleanSupplier listener) {
        ProtectedTerminalEvent protectedEvent = new ProtectedTerminalEvent(caller, this::isActive);
        protectedEvent.connect(listener);
    }

    public <T> void connectSignal(ISignal<T> caller, Consumer<T> listener) {
        ProtectedSignal<T> protectedSignal = new ProtectedSignal<>(caller, this::isActive);
        protectedSignal.connect(listener);
    }

    public <T> void connectTerminalSignal(ITerminalSignal<T> caller, Predicate<T> listener) {
        ProtectedTerminalSignal<T> protectedSignal = new ProtectedTerminalSignal<>(caller, this::isActive);
        protectedSignal.connect(listener);
    }

    protected abstract void onEnter();

    protected abstract void onTick();

    protected abstract void onExit();

    public abstract String getName();

    public boolean isActive() {
        return currentGameMode == this;
    }
}
