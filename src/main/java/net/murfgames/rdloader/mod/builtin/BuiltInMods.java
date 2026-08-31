package net.murfgames.rdloader.mod.builtin;

import net.murfgames.rdloader.mod.Mod;
import net.murfgames.rdloader.mod.builtin.advanced.AdvancedModEntryPoint;
import net.murfgames.rdloader.mod.builtin.debug.DebugModEntryPoint;

public abstract class BuiltInMods {
    public static final Mod DEBUG_MOD = new Mod(
            "rd-loader-debug",
            "Debug",
            "1.0",
            new DebugModEntryPoint()
    );

    public static final Mod ADVANCED_FEATURES_MOD = new Mod(
            "rd-loader-advanced",
            "Advanced Features",
            "1.0",
            new AdvancedModEntryPoint()
    );
}
