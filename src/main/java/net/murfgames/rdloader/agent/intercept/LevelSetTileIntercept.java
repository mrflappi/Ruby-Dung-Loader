package net.murfgames.rdloader.agent.intercept;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.LevelListener;
import net.bytebuddy.implementation.bind.annotation.Argument;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;
import net.murfgames.rdloader.agent.wrapper.LevelWrapper;
import net.murfgames.rdloader.agent.wrapper.RubyDungWrapper;
import net.murfgames.rdloader.level.TileRegistry;

import java.util.List;
import java.util.concurrent.Callable;

public class LevelSetTileIntercept {

    public static boolean intercept(@Argument(0) int x, @Argument(1) int y, @Argument(2) int z, @Argument(3) int type, @SuperCall Callable<Void> callable, @This Object instance) {
        if (!TileRegistry.hasTile(type)) return false;

        Level level = (Level) instance;
        LevelWrapper levelWrapper;
        try {
            levelWrapper = RubyDungWrapper.getLevelWrapper();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        List<LevelListener> listeners;
        try {
            listeners = levelWrapper.getLevelListeners();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        if (type == level.getTile(x, y, z)) {
            return false;
        }

        levelWrapper.setTile(x, y, z, type);
        level.calcLightDepths(x, z, 1, 1);

        int i = 0;
        while (i < listeners.size()) {
            listeners.get(i).tileChanged(x, y, z);
            ++i;
        }
        return true;
    }
}
