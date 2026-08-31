package net.murfgames.rdloader.mod.builtin.debug;

import net.murfgames.rdloader.mod.ModEntryPoint;
import net.murfgames.rdloader.mod.ModPrinter;

public class DebugModEntryPoint implements ModEntryPoint {

    private static final ModPrinter PRINTER = new ModPrinter("DEBUG");

    @Override
    public void onInitialise() {
        PRINTER.println("Debug mod initialised");
    }

    @Override
    public void onTick() {

    }
}
