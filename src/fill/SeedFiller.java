package fill;

import controller.Action;
import model.Point;
import raster.Raster;
import rasterize.SeedRasterizer;

import java.awt.*;
import java.util.OptionalInt;

public class SeedFiller extends Filler {
    private int startX, startY;
    private Color fillPrimaryColor;
    private Color fillSecondaryColor;
    private Color backGroundColor;

    public int getStartX() {
        return startX;
    }
    public int getStartY() {
        return startY;
    }
    public Color getFillPrimaryColor() {
        return fillPrimaryColor;
    }
    public Color getFillSecondaryColor() {return fillSecondaryColor;}
    public Color getBackGroundColor() {
        return backGroundColor;
    }

    public SeedFiller(int startX, int startY, Color fillPrimaryColor, Color fillSecondaryColor, Color backGroundColor){
        this.startX = startX;
        this.startY = startY;
        this.fillPrimaryColor = fillPrimaryColor;
        this.fillSecondaryColor = fillSecondaryColor;
        this.backGroundColor = backGroundColor;
    }

    public void fill(SeedRasterizer rasterizer) {
        rasterizer.rasterize(startX, startY, fillPrimaryColor, fillSecondaryColor, backGroundColor);
    }
}
