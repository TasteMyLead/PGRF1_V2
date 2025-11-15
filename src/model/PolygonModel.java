package model;

import rasterize.LineRasterizer;
import rasterize.PolygonRasterizer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class PolygonModel extends Model {
    protected final List<Point> points;

    public PolygonModel() {
        points = new ArrayList<>();
    }


    public int getIndex(Point p){
        return getIndex(p.getX(), p.getY());
    }

    public int getIndex(int x, int y){
        int index = -1;
        for (int i = 0; i < this.points.size(); i++){
            Point point = this.points.get(i);
            if (isClose(x,y,point.getX(),point.getY(),20)){
                index = i;
            }
        }
        return index;
    }

    public Point getPoint(int index){
        return points.get(index);
    }


    public void setPoint(int x, int y, Color color, int index){
        setPoint(new Point(x, y, color), index);
    }

    public void setPoint(Point point, int index){
        if (index >= 0 && index < this.points.size()){
            points.set(index, point);
        }
    }


    public void addPoint(Point p){
        this.points.add(p);
    }

    public void addPoint(int x, int y, Color color){
        this.points.add(new Point(x, y, color));
    }

    public void removePoint(int index){
        points.remove(index);
    }


    public int getSize(){
        return points.size();
    }

    public int lastIndex(){
        return this.points.size() - 1;
    }


    private boolean isClose(int x1, int y1, int x2, int y2, int toleration){
        double distance = Math.sqrt(Math.pow(x2-x1,2) + Math.pow(y2-y1,2));
        if(distance<toleration) return true;
        else return false;
    }


    public void clear(){
        this.points.clear();
    }


    public void draw(PolygonRasterizer rasterizer){
        rasterizer.rasterize(points);
    };

}
