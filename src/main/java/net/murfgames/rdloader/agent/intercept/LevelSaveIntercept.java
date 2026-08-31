package net.murfgames.rdloader.agent.intercept;

import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;
import net.murfgames.rdloader.RubyDungLoader;
import net.murfgames.rdloader.agent.wrapper.LevelWrapper;
import net.murfgames.rdloader.agent.wrapper.RubyDungWrapper;
import net.murfgames.rdloader.level.LevelData;
import net.murfgames.rdloader.util.Identifier;

import java.io.File;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.zip.GZIPOutputStream;

public class LevelSaveIntercept {
    public static void intercept(@SuperCall Callable<Void> original, @This Object instance) throws Exception {
        try {
            RubyDungLoader.PRINTER.println("Saving level data...");

            LevelWrapper levelWrapper = RubyDungWrapper.getLevelWrapper();
            LevelData levelData = LevelData.createLevelData(levelWrapper);

            ObjectOutputStream outputStream = new ObjectOutputStream(new GZIPOutputStream(Files.newOutputStream(new File("level.dat").toPath())));
            outputStream.writeObject(levelData.file);
            outputStream.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            original.call();
        }
    }
}
