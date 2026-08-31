package net.murfgames.rdloader.audio;

import net.murfgames.rdloader.resource.ResourceManager;
import net.murfgames.rdloader.resource.AudioResource;
import net.murfgames.rdloader.util.Identifier;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

public class AudioMaster {

    private static HashMap<Identifier, Integer> idMap = new HashMap<>();

    public static void init() {
        try {
            AL.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
    }

    public static int loadAudio(Identifier id) throws IOException {
        if (idMap.containsKey(id)) {
            return idMap.get(id);
        }

        int buffer = AL10.alGenBuffers();
        Optional<AudioResource> res = ResourceManager.getAsset(id, AudioResource.class);
        if (!res.isPresent())
            throw new IOException("Specified resource could not be loaded: " + id);

        res.ifPresent(audioResource -> AL10.alBufferData(buffer, audioResource.data.format, audioResource.data.data, audioResource.data.samplerate));
        return buffer;
    }

    public static void setListenerData() {
        AL10.alListener3f(AL10.AL_POSITION, 0, 0, 0);
        AL10.alListener3f(AL10.AL_VELOCITY, 0, 0, 0);
    }
}
