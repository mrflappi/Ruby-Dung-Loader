package net.murfgames.rdloader.level;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.Tesselator;
import com.mojang.minecraft.phys.AABB;
import net.murfgames.rdloader.util.Identifier;

class AirTile extends CustomTile {

    AirTile(Identifier identifier) {
        super(identifier, 0);
    }

    @Override
    public void render(Tesselator t, Level level, int layer, int x, int y, int z) {
        return;
    }

    @Override
    public void renderFace(Tesselator t, int x, int y, int z, int face) {
        return;
    }

    @Override
    public void renderFaceNoTexture(Tesselator t, int x, int y, int z, int face) {
        return;
    }

    @Override
    public boolean isSolid() {
        return false;
    }

    @Override
    public AABB getAABB(int x, int y, int z) {
        return null;
    }

    @Override
    public boolean blocksLight() {
        return false;
    }
}
