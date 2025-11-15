package rasterize;

import fill.SeedFiller;
import raster.RasterBufferedrImage;

import java.awt.*;
import java.util.OptionalInt;

public class SeedRasterizer {
    public RasterBufferedrImage raster;

    private Color fillPrimaryColor;
    private Color fillSecondaryColor;

    private Color backGroundColor;

    public SeedRasterizer(RasterBufferedrImage raster) {
        this.raster = raster;
    }

    public void rasterize(int x, int y, Color fillPrimaryColor,Color fillSecondaryColor, Color backGroundColor){
        this.fillPrimaryColor = fillPrimaryColor;
        this.fillSecondaryColor = fillSecondaryColor;

        this.backGroundColor = backGroundColor;

        fill(x,y);
    }


    private void fill(int x, int y){

        //get barvy
        OptionalInt pixelColor = raster.getPixel(x,y);
        if(pixelColor.isEmpty()) return;

        //podmínky
        if(pixelColor.getAsInt() != backGroundColor.getRGB()){
            return;
        }


        //obarvím

        if (fillPrimaryColor == fillSecondaryColor || fillSecondaryColor == null) {
            raster.setPixel(x,y,fillPrimaryColor.getRGB());
        }
        else{
            if(x%2 == 0){
                raster.setPixel(x,y,fillSecondaryColor.getRGB());
            }
            else{
                raster.setPixel(x,y,fillPrimaryColor.getRGB());
            }
        }


        //seedFill zavolám pro sousedy
        //Barvím sousedy to je SeedFiller
        this.fill(x,y+1);
        this.fill(x,y-1);
        this.fill(x+1,y);
        this.fill(x-1,y);
    }
}
