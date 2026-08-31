package net.murfgames.rdloader.mod.builtin.advanced;

import net.murfgames.rdloader.KeyboardEventHandler;
import net.murfgames.rdloader.agent.wrapper.RubyDungWrapper;
import net.murfgames.rdloader.gamemode.GameMode;
import net.murfgames.rdloader.level.TileRegistry;
import net.murfgames.rdloader.util.Identifier;
import org.lwjgl.input.Keyboard;

public class CreativeGameMode extends GameMode {

    public CreativeGameMode(Identifier identifier) {
        super(identifier);
        this.connectTerminalSignal(KeyboardEventHandler.EVENT_KEY_SIGNAL, this::onEventKey);
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
        return "Survival";
    }

    private boolean onEventKey(int keyCode) {
        int tile;
        if (Keyboard.KEY_1 < keyCode && keyCode <= Keyboard.KEY_0)
            tile = keyCode - 1;
        else
            return false;

        try {
            if (TileRegistry.hasTile(tile))
                RubyDungWrapper.setPaintTexture(tile);
            else
                RubyDungWrapper.setPaintTexture(0);
            return true;
        } catch (IllegalAccessException e) {
            AdvancedModEntryPoint.PRINTER.printerr("Error while trying to set item tile", e);
            return false;
        }
    }
}
