package net.murfgames.rdloader.agent.intercept;

import com.mojang.minecraft.character.Zombie;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.murfgames.rdloader.KeyboardEventHandler;
import net.murfgames.rdloader.RubyDungLoader;
import net.murfgames.rdloader.agent.wrapper.RubyDungWrapper;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class RubyDungTickIntercept {

    public static void intercept(@SuperCall Callable<Void> original) {
        RubyDungLoader.preTick();

        try {
            RubyDungWrapper.getLevelWrapper().level.tick();
            RubyDungWrapper.getParticleEngine().tick();

            int i = 0;
            ArrayList<Zombie> zombies = RubyDungWrapper.getZombies();
            while (i < zombies.size()) {
                zombies.get(i).tick();
                if (zombies.get(i).removed) {
                    zombies.remove(i--);
                }
                ++i;
            }
            RubyDungWrapper.getPlayerWrapper().player.tick();

        } catch (Exception e) {
            RubyDungLoader.PRINTER.printerr("Failed to tick", e);
        }

        RubyDungLoader.postTick();
    }
}
