package net.murfgames.rdloader.agent.intercept;

import net.bytebuddy.implementation.bind.annotation.Argument;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;
import net.murfgames.rdloader.agent.wrapper.LevelWrapper;
import net.murfgames.rdloader.agent.wrapper.RubyDungWrapper;

import java.util.concurrent.Callable;

public class LevelGetTileIntercept {

    public static int intercept(@Argument(0) int x, @Argument(1) int y, @Argument(2) int z, @SuperCall Callable<Void> callable, @This Object instance) {
        LevelWrapper levelWrapper;
        try {
            levelWrapper = RubyDungWrapper.getLevelWrapper();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return levelWrapper.getTile(x, y, z);
    }
}
