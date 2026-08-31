package net.murfgames.rdloader.audio;

import net.murfgames.rdloader.RubyDungLoader;
import org.lwjgl.openal.AL10;

public class PositionalAudioSource extends AudioSource {
    private static final float REFERENCE_DISTANCE = 1.0f;
    private static final float MAX_DISTANCE = 8.0f;
    private static final int ROLLOFF_FACTOR = 2;

    public PositionalAudioSource() {
        super();

        AL10.alSourcef(sourceId, AL10.AL_REFERENCE_DISTANCE, REFERENCE_DISTANCE);
        AL10.alSourcef(sourceId, AL10.AL_MAX_DISTANCE, MAX_DISTANCE);
        AL10.alSourcei(sourceId, AL10.AL_ROLLOFF_FACTOR, ROLLOFF_FACTOR);
    }

    /**
     * Plays a sound at a specific position in the world.
     * @param buffer The audio buffer to play.
     * @param x The x-coordinate of the tile.
     * @param y The y-coordinate of the tile.
     * @param z The z-coordinate of the tile.
     * @param playerX The x-coordinate of the player.
     * @param playerY The y-coordinate of the player.
     * @param playerZ The z-coordinate of the player.
     * @param playerRotationYaw The player's yaw rotation (in degrees).
     */
    public void playAt(
            int buffer,
            float x, float y, float z,
            float playerX, float playerY, float playerZ,
            float playerRotationYaw
    ) {

        float relativeX = x - playerX;
        float relativeZ = z - playerZ;
        float relativeY = y - playerY;
        float yawRad = (float) Math.toRadians(playerRotationYaw) * -1.0f;

        float rotatedX = relativeX * (float) Math.cos(yawRad) - relativeZ * (float) Math.sin(yawRad);
        float rotatedZ = relativeX * (float) Math.sin(yawRad) + relativeZ * (float) Math.cos(yawRad);
        AL10.alSource3f(sourceId, AL10.AL_POSITION, playerX - rotatedX, playerY + relativeY, playerZ + rotatedZ);

        play(buffer);
    }

    /**
     * Plays a sound at a specific position in the world, using the listener's position and rotation.
     * @param buffer The audio buffer to play.
     * @param tileX The x-coordinate of the tile.
     * @param tileY The y-coordinate of the tile.
     * @param tileZ The z-coordinate of the tile.
     */
    public void playAt(
            int buffer,
            float tileX, float tileY, float tileZ
    ) {
        playAt(buffer, tileX, tileY, tileZ, 0, 0, 0, 0);
    }
}
