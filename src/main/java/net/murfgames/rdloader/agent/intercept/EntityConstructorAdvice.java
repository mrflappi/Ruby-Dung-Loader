package net.murfgames.rdloader.agent.intercept;

import com.mojang.minecraft.Player;
import net.bytebuddy.asm.Advice;
import net.murfgames.rdloader.agent.wrapper.EntityWrapper;
import net.murfgames.rdloader.agent.wrapper.RubyDungWrapper;

public class EntityConstructorAdvice {
    @Advice.OnMethodExit
    static void onExit(@Advice.This Object instance) {
        if (instance instanceof Player)
            return;

        EntityWrapper entityWrapper = new EntityWrapper(instance);
        RubyDungWrapper.cacheEntityWrapper(entityWrapper);
    }
}
