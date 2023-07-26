import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.IOException;

public class BufferedImageLoader extends Image {

    private BufferedImage image;

    public BufferedImage loadImage(String path){
        try {
            image = ImageIO.read(getClass().getResource(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    return image;
    }

    @Override
    public int getWidth(ImageObserver observer) {
        return 0;
    }

    @Override
    public int getHeight(ImageObserver observer) {
        return 0;
    }

    @Override
    public ImageProducer getSource() {
        return null;
    }

    @Override
    public Graphics getGraphics() {
        return null;
    }

    @Override
    public Object getProperty(String name, ImageObserver observer) {
        return null;
    }
}
