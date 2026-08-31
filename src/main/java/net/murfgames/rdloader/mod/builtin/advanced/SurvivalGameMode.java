package net.murfgames.rdloader.mod.builtin.advanced;

import net.murfgames.rdloader.KeyboardEventHandler;
import net.murfgames.rdloader.agent.wrapper.RubyDungWrapper;
import net.murfgames.rdloader.gamemode.DefaultGameMode;
import net.murfgames.rdloader.gamemode.GameMode;
import net.murfgames.rdloader.util.Identifier;

public class SurvivalGameMode extends GameMode {

    public SurvivalGameMode(Identifier identifier) {
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
        int i;
        if (1 < keyCode && keyCode < 11)
            i = keyCode - 2;
        else
            return false;

        try {
            int tile = AdvancedModEntryPoint.PLAYER_INVENTORY.getItemTile(i);
            RubyDungWrapper.setPaintTexture(tile);
            return true;
        } catch (IllegalAccessException e) {
            AdvancedModEntryPoint.PRINTER.printerr("Error while trying to set item tile", e);
            return false;
        }
    }
}
