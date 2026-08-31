package net.murfgames.rdloader.resource;

import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;

import java.io.*;
import java.nio.file.Files;

public class AudioResource extends Resource<WaveData> {

    public AudioResource(WaveData data) {
        super(data);
    }

    public static AudioResource load(File file) throws IOException {
        try (InputStream in = new BufferedInputStream(Files.newInputStream(file.toPath()))) {
            return load(in);
        }
    }

    public static AudioResource load(InputStream in) throws IOException {
        int preError = AL10.alGetError();
        if (preError != AL10.AL_NO_ERROR) {
            throw new IOException("OpenAL error before loading: " + preError);
        }

        WaveData waveFile = WaveData.create(in);
        if (waveFile == null) {
            int alErr = AL10.alGetError();
            throw new IOException("Failed to read WAV data: " + alErr);
        }

        return new AudioResource(waveFile);
    }
}
