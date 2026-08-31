package net.murfgames.rdloader.level;

import com.mojang.minecraft.level.tile.Tile;
import net.murfgames.rdloader.util.Identifier;

public class CustomTile extends Tile {
    public final Identifier identifier;

    public CustomTile(Identifier identifier, int tex) {
        super(tiles.length - 1);
        tiles[tiles.length - 1] = null;

        this.identifier = identifier;
        this.tex = tex;
    }
}
