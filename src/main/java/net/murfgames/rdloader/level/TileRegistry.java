package net.murfgames.rdloader.level;

import com.mojang.minecraft.level.tile.Tile;
import net.murfgames.rdloader.RubyDungLoader;
import net.murfgames.rdloader.util.Identifier;

import java.util.*;

public class TileRegistry {
    private static final Map<Identifier, Tile> tiles = new HashMap<>();
    private static final List<Identifier> idMap = new ArrayList<>();
    private static final AirTile air = new AirTile(new Identifier("air"));

    static {
        registerTile(air);
        registerTile(Tile.rock, new Identifier("rock"));
        registerTile(Tile.grass, new Identifier("grass"));
        registerTile(Tile.dirt, new Identifier("dirt"));
        registerTile(Tile.stoneBrick, new Identifier("stone_brick"));
        registerTile(Tile.wood, new Identifier("wood"));
        registerTile(Tile.bush, new Identifier("bush"));
    }

    private static void registerTile(Tile tile, Identifier identifier) {
        if (RubyDungLoader.initialised) return;
        idMap.add(identifier);
        tiles.put(identifier, tile);
    }

    public static void registerTile(CustomTile tile) {
        registerTile(tile, tile.identifier);
    }

    public static Tile getTile(int id) {
        Identifier identifier = idMap.size() > id ? idMap.get(id) : null;
        return tiles.getOrDefault(identifier, air);
    }

    public static Tile getTile(Identifier id) {
        return tiles.getOrDefault(id, air);
    }

    public static Identifier convertId(int id) {
        if (id >= idMap.size() || id < 0)
            return idMap.get(0);
        return idMap.get(id);
    }

    public static int convertId(Identifier id) {
        if (idMap.contains(id))
            return idMap.indexOf(id);
        return 0;
    }

    public static boolean hasTile(int i) {
        return i < idMap.size();
    }

    public static int tileCount() {
        return idMap.size();
    }
}
