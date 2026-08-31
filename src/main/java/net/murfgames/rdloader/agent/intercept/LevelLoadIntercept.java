package net.murfgames.rdloader.agent.intercept;

import com.mojang.minecraft.level.Level;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;
import net.murfgames.rdloader.agent.wrapper.LevelWrapper;
import net.murfgames.rdloader.agent.wrapper.RubyDungWrapper;
import net.murfgames.rdloader.level.LevelData;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.concurrent.Callable;
import java.util.zip.GZIPInputStream;

public class LevelLoadIntercept {
    public static boolean intercept(@SuperCall Callable<Boolean> original, @This Object instance) throws Exception {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new GZIPInputStream(new FileInputStream(new File("level.dat"))));
            LevelData levelData = new LevelData((LevelData.FileWrapper) inputStream.readObject());

            LevelWrapper levelWrapper = new LevelWrapper(instance, levelData);
            RubyDungWrapper.cacheLevelWrapper(levelWrapper);
            Level level = levelWrapper.level;
            try {
                level.calcLightDepths(0, 0, level.width, level.height);
            } catch (Exception e) {
                e.printStackTrace();
            }
            int i = 0;
            while (i < levelWrapper.getLevelListeners().size()) {
                levelWrapper.getLevelListeners().get(i).allChanged();
                ++i;
            }

            inputStream.close();
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            RubyDungWrapper.cacheLevelWrapper(new LevelWrapper(instance));
            return original.call();
        }
    }
}
