package net.murfgames.rdloader.level;

import net.murfgames.rdloader.agent.wrapper.LevelWrapper;
import net.murfgames.rdloader.util.Identifier;
import net.murfgames.rdloader.util.event.ProtectedSignal;
import net.murfgames.rdloader.util.event.Signal;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

// A custom level data class - allows for saving other data than just tiles
public class LevelData {
    public final FileWrapper file;

    private static final Signal<Builder> DATA_BUILD = new Signal<>();
    public static final ProtectedSignal<Builder> DATA_BUILD_SIGNAL = new ProtectedSignal<>(DATA_BUILD);

    public LevelData(FileWrapper file) {
        this.file = file;
    }

    public Identifier[][][] getTileIds() {
        return file.tiles;
    }

    public Map<Identifier, Object> getData() {
        return file.data;
    }

    public int[][][] mappedTileIds() {
        Identifier[][][] tiles = getTileIds();
        int[][][] convertedIds = new int[tiles.length][tiles[0].length][tiles[0][0].length];

        for(int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[x].length; y++) {
                for (int z = 0; z < tiles[x][y].length; z++) {
                    Identifier id = tiles[x][y][z];

                    if (id != null) {
                        convertedIds[x][y][z] = TileRegistry.convertId(id);
                    }
                }
            }
        }

        return convertedIds;
    }

    public static LevelData createLevelData(LevelWrapper levelWrapper) {
        Builder builder = new Builder(levelWrapper);
        DATA_BUILD.emit(builder);
        return builder.build();
    }

    public static class Builder {
        public final LevelWrapper levelWrapper;
        private final Identifier[][][] tiles;
        private final Map<Identifier, Object> data;

        public Builder(LevelWrapper levelWrapper) {
            this.levelWrapper = levelWrapper;
            this.tiles = convertTileIds(levelWrapper.getTiles());
            this.data = new HashMap<>();
        }

        private Identifier[][][] convertTileIds(int[][][] tiles) {
            Identifier[][][] convertedIds = new Identifier[tiles.length][tiles[0].length][tiles[0][0].length];

            for(int x = 0; x < tiles.length; x++){
                for(int y = 0; y < tiles[x].length; y++){
                    for(int z = 0; z < tiles[x][y].length; z++){
                        int id = tiles[x][y][z];
                        if (id > 0 && id < TileRegistry.tileCount() && TileRegistry.hasTile(id)) {
                            Identifier convertedId = TileRegistry.convertId(id);
                            convertedIds[x][y][z] = convertedId;
                        }
                    }
                }
            }

            return convertedIds;
        }

        public LevelData build() {
            FileWrapper file = new FileWrapper(tiles, Collections.unmodifiableMap(data));
            return new LevelData(file);
        }

        public void registerData(Identifier id, Object value) {
            data.put(id, value);
        }
    }

    public static class FileWrapper implements Serializable {
        public final Identifier[][][] tiles;
        public final Map<Identifier, Object> data;

        public FileWrapper(Identifier[][][] tiles, Map<Identifier, Object> data) {
            this.tiles = tiles;
            this.data = data;
        }
    }
}
