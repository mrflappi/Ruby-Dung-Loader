package net.murfgames.rdloader.agent.wrapper;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.LevelListener;
import net.murfgames.rdloader.level.LevelData;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public final class LevelWrapper {
    public final Level level;
    public final LevelData levelData;
    private final int[][][] tiles;

    private static final Field blocks;
    private static final Field levelListeners;

    static {
        try {
            blocks = Level.class.getDeclaredField("blocks");
            blocks.setAccessible(true);

            levelListeners = Level.class.getDeclaredField("levelListeners");
            levelListeners.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public LevelWrapper(Object instance) {
        this.level = (Level) instance;
        this.tiles = new int[level.width][level.depth][level.height];
        this.levelData = new LevelData.Builder(this).build();
    }

    public LevelWrapper(Object instance, LevelData levelData) {
        this.level = (Level) instance;
        this.tiles = levelData.mappedTileIds();
        this.levelData = levelData;
    }

    public byte[] getBlocks() throws IllegalAccessException {
        return (byte[]) blocks.get(level);
    }

    @SuppressWarnings("unchecked")
    public ArrayList<LevelListener> getLevelListeners() throws IllegalAccessException {
        return (ArrayList<LevelListener>) levelListeners.get(level);
    }

    public void setTile(int x, int y, int z, int id) {
        if (x < 0 || y < 0 || z < 0 || x >= level.width || y >= level.depth || z >= level.height)
            return;
        tiles[x][y][z] = id;
    }

    public int getTile(int x, int y, int z) {
        if (x < 0 || y < 0 || z < 0 || x >= level.width || y >= level.depth || z >= level.height)
            return 0;
        return tiles[x][y][z];
    }

    public int[][][] getTiles() {
        return tiles;
    }
}
