package net.murfgames.rdloader.gamemode;

import net.murfgames.rdloader.util.Identifier;

public class DefaultGameMode extends GameMode {

    public static final DefaultGameMode INSTANCE = new DefaultGameMode(new Identifier("default"));

    static {
        GameMode.registerGameMode(INSTANCE);
    }

    public DefaultGameMode(Identifier identifier) {
        super(identifier);
    }

    @Override
    protected void onEnter() {

    }

    @Override
    protected void onTick() {

    }

    @Override
    protected void onExit() {

    }

    @Override
    public String getName() {
        return "Default";
    }
}
