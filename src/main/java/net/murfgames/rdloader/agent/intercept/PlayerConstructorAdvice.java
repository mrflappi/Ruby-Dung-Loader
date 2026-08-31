package net.murfgames.rdloader.agent.intercept;

import net.bytebuddy.asm.Advice;
import net.murfgames.rdloader.agent.wrapper.PlayerWrapper;
import net.murfgames.rdloader.agent.wrapper.RubyDungWrapper;

public class PlayerConstructorAdvice {

    @Advice.OnMethodExit
    static void onExit(@Advice.This Object instance) {
        PlayerWrapper playerWrapper = new PlayerWrapper(instance);
        RubyDungWrapper.cachePlayerWrapper(playerWrapper);
    }
}
