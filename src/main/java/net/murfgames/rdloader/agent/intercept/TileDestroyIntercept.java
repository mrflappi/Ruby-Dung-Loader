package net.murfgames.rdloader.agent.intercept;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.particle.ParticleEngine;
import net.bytebuddy.implementation.bind.annotation.Argument;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;
import net.murfgames.rdloader.util.event.ProtectedSignal;
import net.murfgames.rdloader.util.event.Signal;

import java.util.concurrent.Callable;

public class TileDestroyIntercept {

    private static final Signal<DestroyData> DESTROY = new Signal<>();
    public static final ProtectedSignal<DestroyData> DESTROY_SIGNAL = new ProtectedSignal<>(DESTROY);

    public static void intercept(@Argument(0) Level level, @Argument(1) int x, @Argument(2) int y, @Argument(3) int z, @Argument(4) ParticleEngine particleEngine, @SuperCall Callable<Void> original, @This Object instance) throws Exception {
        original.call();
        DESTROY.emit(new DestroyData((Tile) instance, level, x, y, z));
    }

    public static class DestroyData {
        public final Tile tile;
        public final Level level;
        public final int x;
        public final int y;
        public final int z;

        DestroyData(Tile tile, Level level, int x, int y, int z) {
            this.tile = tile;
            this.level = level;
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
}
