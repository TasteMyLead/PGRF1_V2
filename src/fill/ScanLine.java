package fill;

import model.Edge;
import model.Point;
import model.PolygonModel;
import rasterize.LineRasterizer;
import rasterize.ScanLineRasterizer;
import rasterize.SeedRasterizer;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class ScanLine extends Filler{

    private final PolygonModel polygonModel;
    private final Color primaryColor;
    private final Color secondaryColor;

    public ScanLine(PolygonModel polygonModel, Color primaryColor, Color secondaryColor) {
        this.polygonModel = polygonModel;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
    }

    public void fill(ScanLineRasterizer rasterizer) {
        rasterizer.rasterize(polygonModel, primaryColor, secondaryColor);
    }

}
