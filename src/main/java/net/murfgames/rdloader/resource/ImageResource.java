package net.murfgames.rdloader.resource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ImageResource extends Resource<BufferedImage> {

    public ImageResource(BufferedImage data) {
        super(data);
    }

    public static ImageResource load(File file) throws IOException {
        BufferedImage img = ImageIO.read(file);
        return new ImageResource(img);
    }

    public static ImageResource load(InputStream in) throws IOException {
        BufferedImage img = ImageIO.read(in);
        return new ImageResource(img);
    }
}
