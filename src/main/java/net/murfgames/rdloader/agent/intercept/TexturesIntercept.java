package net.murfgames.rdloader.agent.intercept;


import net.bytebuddy.implementation.bind.annotation.Argument;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.murfgames.rdloader.resource.ImageResource;
import net.murfgames.rdloader.resource.ResourceManager;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.Callable;

public class TexturesIntercept {

    private static Field idMap;

    @SuppressWarnings("unchecked")
    private static HashMap<String, Integer> getIdMap() {
        try {
            if (idMap != null) return (HashMap<String, Integer>) idMap.get(null);

            Class<?> texturesClass = Class.forName("com.mojang.minecraft.Textures");
            Field field = texturesClass.getDeclaredField("idMap");
            field.setAccessible(true);
            return (HashMap<String, Integer>) field.get(null);
        } catch (Exception e) {
            throw new RuntimeException("idMap could not be accessed", e);
        }
    }

    public static int intercept(@Argument(0) String resourceName, @Argument(1) int mode, @SuperCall Callable<Integer> original) {
        if (getIdMap().containsKey(resourceName)) {
            return (Integer)getIdMap().get(resourceName);
        } else {
            IntBuffer intBuffer = BufferUtils.createIntBuffer(1);
            intBuffer.clear();
            GL11.glGenTextures(intBuffer);
            int id = intBuffer.get(0);

            getIdMap().put(resourceName, id);

            GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, mode);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, mode);

            Optional<ImageResource> res = ResourceManager.getAsset(resourceName, ImageResource.class);
            BufferedImage img = res.get().data;

            int width = img.getWidth();
            int height = img.getHeight();
            ByteBuffer pixels = BufferUtils.createByteBuffer(width * height * 4);
            int[] rawPixels = new int[width * height];
            img.getRGB(0, 0, width, height, rawPixels, 0, width);

            for(int i = 0; i < rawPixels.length; ++i) {
                int a = rawPixels[i] >> 24 & 255;
                int r = rawPixels[i] >> 16 & 255;
                int g = rawPixels[i] >> 8 & 255;
                int b = rawPixels[i] & 255;
                rawPixels[i] = a << 24 | b << 16 | g << 8 | r;
            }

            pixels.asIntBuffer().put(rawPixels);
            GLU.gluBuild2DMipmaps(3553, 6408, width, height, 6408, 5121, pixels);
            return id;
        }
    }
}
