package model;
import rasterize.LineRasterizer;

import java.awt.Color;

public class Line extends Model {
    private final Point startPoint;
    private final Point endPoint;

    public Line(int x1, int y1, int x2, int y2, Color color1, Color color2) {
        this.startPoint = new Point(x1,y2,color1);
        this.endPoint = new Point(x1,y1,color2);
    }

    public Line (Point p1, Point p2) {
       this.startPoint = p1;
       this.endPoint = p2;
    }

    public Line(int x1, int y1, int x2, int y2, Color color1) {
        this.startPoint = new Point(x1,y1,color1);
        this.endPoint = new Point(x2,y2,color1);
    }


    public int getX1() {
        return startPoint.getX();
    }
    public int getY1() {
        return startPoint.getY();
    }
    public int getX2() {
        return endPoint.getX();
    }
    public int getY2() {
        return endPoint.getY();
    }

    public Point getStartPoint() {
        return startPoint;
    }
    public Point getEndPoint() {
        return endPoint;
    }

    public Color getColor() {
        return startPoint.getColor();
    }

    public int getChoosenPoint(int x1, int y1){
        if (isClose(startPoint.getX(), startPoint.getY(), x1, y1, 30)){
            return 0;
        }
        else if (isClose(endPoint.getX(), endPoint.getY(), x1, y1, 30)){
            return 1;
        }
        return -1;
    }

    public boolean isClose(int x1, int y1, int x2, int y2, int toleration){
        double distance = Math.sqrt(Math.pow(x2-x1,2) + Math.pow(y2-y1,2));
        if(distance<toleration) return true;
        else return false;
    }


    public void draw(LineRasterizer rasterizer){
        rasterizer.rasterize(startPoint, endPoint, startPoint.getColor());
    }

}
