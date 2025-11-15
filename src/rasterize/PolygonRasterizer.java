package rasterize;

import model.Point;
import model.Polygon;
import model.PolygonModel;

import java.util.List;

public class PolygonRasterizer {
    private LineRasterizer lineRasterizer;

    public PolygonRasterizer(LineRasterizer lineRasterizer){
        this.lineRasterizer = lineRasterizer;
    }

    public void setLineRasterizer(LineRasterizer lineRasterizer){
        this.lineRasterizer = lineRasterizer;
    }

    public void rasterize(PolygonModel polygon){
        //Pokud je méně jak 3 pointy, nevykresluju
        if (polygon.getSize() > 2){
            for(int i = 0; i <= polygon.lastIndex(); i++){

                Point p1 = polygon.getPoint(i);

                if (i == polygon.lastIndex()) {
                    Point p2 = polygon.getPoint(0);
                    lineRasterizer.rasterize(p1,p2,p1.getColor());
                }
                else{
                    Point p2 = polygon.getPoint(i+1);
                    lineRasterizer.rasterize(p1,p2,p1.getColor());
                }
            }
        }
    }

    public void rasterize(List<Point> points){
        if (points.size() > 2){
            for(int i = 0; i <= points.size()-1; i++){
                Point p1 = points.get(i);
                Point p2;
                if (i == points.size()-1) {
                    p2 = points.get(0);
                }else{
                    p2 = points.get(i+1);
                }
                lineRasterizer.rasterize(p1,p2,p1.getColor());
            }
        }
    }
}
