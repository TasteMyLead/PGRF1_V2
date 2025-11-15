package raster;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.OptionalInt;

public class RasterBufferedrImage implements Raster {
    private BufferedImage image;

    public RasterBufferedrImage(int  width, int height) {
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        clear();
    }



    @Override
    public void setPixel(int x, int y, int color) {
        image.setRGB(x, y, color);
    }

    @Override
    public OptionalInt getPixel(int x, int y) {
        if (x < 0 || y < 0 || x >= image.getWidth() || y >= image.getHeight()) {
            return OptionalInt.empty();
        }
        return OptionalInt.of(image.getRGB(x,y));
    }

    @Override
    public int getWidth() {
        return image.getWidth();
    }

    @Override
    public int getHeight() {
        return image.getHeight();
    }

    @Override
    public void clear() {
        Graphics g = image.getGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, image.getWidth(), image.getHeight());
        g.dispose();
    }

    public BufferedImage getImage() {
        return image;
    }
}
