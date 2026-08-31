package net.murfgames.rdloader.agent.intercept;

import com.mojang.minecraft.Entity;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;
import net.murfgames.rdloader.agent.wrapper.EntityWrapper;
import net.murfgames.rdloader.agent.wrapper.RubyDungWrapper;
import org.lwjgl.openal.AL10;

import java.util.concurrent.Callable;

public class PlayerTickIntercept {
    public static void intercept(@SuperCall Callable<Void> original, @This Object instance) {
        try {
            original.call();
            EntityWrapper entityWrapper = RubyDungWrapper.getEntityWrapper((Entity) instance);

            float x = entityWrapper.getX();
            float y = entityWrapper.getY();
            float z = entityWrapper.getZ();

            AL10.alListener3f(AL10.AL_POSITION, x, y, z);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
