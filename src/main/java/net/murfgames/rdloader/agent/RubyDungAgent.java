package net.murfgames.rdloader.agent;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import net.murfgames.rdloader.RubyDungLoader;
import net.murfgames.rdloader.agent.intercept.*;

import java.lang.instrument.Instrumentation;

public class RubyDungAgent {
    private static boolean installed = false;

    public static void premain(String agentArgs, Instrumentation inst) {
        if (installed) return;
        RubyDungLoader.PRINTER.println("Injecting into RubyDung...");

        new AgentBuilder.Default()
                .ignore(ElementMatchers.none())
                .type(ElementMatchers.named("com.mojang.minecraft.RubyDung"))
                .transform((builder, typeDescription, classLoader, module, protectionDomain) ->
                        builder
                                .method(ElementMatchers.named("init"))
                                .intercept(MethodDelegation.to(RubyDungInitIntercept.class))

                                .method(ElementMatchers.named("tick"))
                                .intercept(MethodDelegation.to(RubyDungTickIntercept.class))

                                .visit(TilesArraySubstitution.create())
                )
                .type(ElementMatchers.named("com.mojang.minecraft.Textures"))
                .transform(((builder, typeDescription, classLoader, module, protectionDomain) ->
                        builder
                                .method(ElementMatchers.named("loadTexture"))
                                .intercept(MethodDelegation.to(TexturesIntercept.class)))
                )
                .type(ElementMatchers.named("com.mojang.minecraft.level.Level"))
                .transform((builder, typeDescription, classLoader, module, protectionDomain) ->
                        builder
                                .method(ElementMatchers.named("load"))
                                .intercept(MethodDelegation.to(LevelLoadIntercept.class))

                                .method(ElementMatchers.named("save"))
                                .intercept(MethodDelegation.to(LevelSaveIntercept.class))

                                .method(ElementMatchers.named("getTile"))
                                .intercept(MethodDelegation.to(LevelGetTileIntercept.class))

                                .method(ElementMatchers.named("setTile"))
                                .intercept(MethodDelegation.to(LevelSetTileIntercept.class))

                                .method(ElementMatchers.named("generateMap"))
                                .intercept(MethodDelegation.to(LevelGenerateMapIntercept.class))

                                .visit(TilesArraySubstitution.create())
                )
                .type(ElementMatchers.named("com.mojang.minecraft.level.LevelRenderer"))
                .transform((builder, typeDescription, classLoader, module, protectionDomain) ->
                        builder
                                .visit(TilesArraySubstitution.create())
                )
                .type(ElementMatchers.named("com.mojang.minecraft.level.Chunk"))
                .transform((builder, typeDescription, classLoader, module, protectionDomain) ->
                        builder
                                .visit(TilesArraySubstitution.create())
                )
                .type(ElementMatchers.named("com.mojang.minecraft.Player"))
                .transform((builder, typeDescription, classLoader, module, protectionDomain) ->
                        builder
                                .constructor(ElementMatchers.any())
                                .intercept(Advice.to(PlayerConstructorAdvice.class))

                                .method(ElementMatchers.named("tick"))
                                .intercept(MethodDelegation.to(PlayerTickIntercept.class))
                )
                .type(ElementMatchers.named("com.mojang.minecraft.level.tile.Tile"))
                .transform((builder, typeDescription, classLoader, module, protectionDomain) ->
                        builder
                                .method(ElementMatchers.named("destroy"))
                                .intercept(MethodDelegation.to(TileDestroyIntercept.class))
                )
                .type(ElementMatchers.named("com.mojang.minecraft.Entity"))
                .transform((builder, typeDescription, classLoader, module, protectionDomain) ->
                        builder
                                .constructor(ElementMatchers.any())
                                .intercept(Advice.to(EntityConstructorAdvice.class))
                )
                .installOn(inst);
        installed = true;
    }
}
