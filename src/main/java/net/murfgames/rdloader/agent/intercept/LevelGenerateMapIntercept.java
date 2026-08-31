package net.murfgames.rdloader.agent.intercept;

import com.mojang.minecraft.level.Level;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;
import net.murfgames.rdloader.agent.wrapper.LevelWrapper;
import net.murfgames.rdloader.agent.wrapper.RubyDungWrapper;

import java.util.concurrent.Callable;

public class LevelGenerateMapIntercept {
    public static void intercept(@SuperCall Callable<Void> original, @This Object instance) throws Exception {
        Level level = (Level) instance;
        LevelWrapper levelWrapper;

        try {
            levelWrapper = RubyDungWrapper.getLevelWrapper();
        } catch (IllegalAccessException e) {
            levelWrapper = new LevelWrapper(level);
            RubyDungWrapper.cacheLevelWrapper(levelWrapper);
        }

        original.call();

        int w = level.width;
        int h = level.height;
        int d = level.depth;

        int x = 0;
        while (x < w) {
            int y = 0;
            while (y < d) {
                int z = 0;
                while (z < h) {
                    byte id = levelWrapper.getBlocks()[(y * level.height + z) * level.width + x];
                    levelWrapper.setTile(x, y, z, id);
                    ++z;
                }
                ++y;
            }
            ++x;
        }
    }
}
