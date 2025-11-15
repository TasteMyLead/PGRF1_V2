package view;

import model.Point;
import raster.RasterBufferedrImage;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Canvas extends JPanel {
    private final RasterBufferedrImage raster;

    private int width;
    private int height;

    public Canvas(int width, int height) {
        raster = new RasterBufferedrImage(width, height);
        raster.clear();

        this.width = width;
        this.height = height;

        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(raster.getImage(), 0, 0, null);
    }

    @Override
    public int getHeight() {
        return height;
    }
    @Override
    public int getWidth() {
        return width;
    }

    public RasterBufferedrImage getRaster() {return raster;}

    public BufferedImage getBufferedImage() {
        return raster.getImage();
    }

    //Zjistí jestli je mimo canvas
    public boolean isOutOfBounderies(int x, int y){
        if(x<0 || y<0 || x>getWidth()-1 || y>getHeight()-1) {
            return true;
        }
        return false;
    }

    public boolean isOutOfBounderies(Point p){
        if(isOutOfBounderies(p.getX(), p.getY())){
            return true;
        }

        return false;
    }

    public boolean isOutOfBounderiesHeight(int y){
        if(y<0 || y>getHeight()-1) {
            return true;
        }

        return false;
    }

    public boolean isOutOfBounderiesWidth(int x){
        if(x<0 || x>getWidth()-1) {
            return true;
        }
        return false;
    }
}
