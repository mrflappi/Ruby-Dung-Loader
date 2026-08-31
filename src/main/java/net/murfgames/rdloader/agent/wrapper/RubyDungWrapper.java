package net.murfgames.rdloader.agent.wrapper;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.RubyDung;
import com.mojang.minecraft.character.Zombie;
import com.mojang.minecraft.particle.ParticleEngine;
import net.murfgames.rdloader.RubyDungLoader;
import net.murfgames.rdloader.level.TileRegistry;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;
import java.util.WeakHashMap;

public abstract class RubyDungWrapper {
    private static RubyDung rubyDungInstance;

    private static final Field paintTexture;
    private static final Field zombies;
    private static final Field particleEngine;

    private static LevelWrapper levelWrapper;
    private static PlayerWrapper playerWrapper;
    private static final Map<Entity, EntityWrapper> entityWrapperCache = new WeakHashMap<>();

    static {
        try {
            paintTexture = RubyDung.class.getDeclaredField("paintTexture");
            paintTexture.setAccessible(true);

            zombies = RubyDung.class.getDeclaredField("zombies");
            zombies.setAccessible(true);

            particleEngine = RubyDung.class.getDeclaredField("particleEngine");
            particleEngine.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static void accessRubyDung(Object instance) throws NoSuchFieldException {
        RubyDungLoader.PRINTER.println("Accessing RubyDung...");

        rubyDungInstance = (RubyDung) instance;
    }

    public static void cacheLevelWrapper(LevelWrapper wrapper) {
        if (levelWrapper == null) levelWrapper = wrapper;
    }

    public static void cachePlayerWrapper(PlayerWrapper wrapper) {
        if (playerWrapper == null) {
            playerWrapper = wrapper;
            cacheEntityWrapper(wrapper);
        }
    }

    public static void cacheEntityWrapper(EntityWrapper wrapper) {
        entityWrapperCache.put(wrapper.entity, wrapper);
    }

    public static LevelWrapper getLevelWrapper() throws IllegalAccessException {
        if (levelWrapper != null)
            return levelWrapper;

        throw new RuntimeException();
    }

    public static PlayerWrapper getPlayerWrapper() {
        if (playerWrapper != null)
            return playerWrapper;

        throw new RuntimeException();
    }

    public static EntityWrapper getEntityWrapper(Entity entity) {
        if (entityWrapperCache.containsKey(entity))
            return entityWrapperCache.get(entity);
        throw new RuntimeException();
    }

    public static int getPaintTexture() throws IllegalAccessException {
        return paintTexture.getInt(rubyDungInstance);
    }

    public static void setPaintTexture(int i) throws IllegalAccessException {
        if (TileRegistry.hasTile(i))
            paintTexture.setInt(rubyDungInstance, i);
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<Zombie> getZombies() throws IllegalAccessException {
        return (ArrayList<Zombie>) zombies.get(rubyDungInstance);
    }

    public static ParticleEngine getParticleEngine() throws IllegalAccessException {
        return (ParticleEngine) particleEngine.get(rubyDungInstance);
    }
}
