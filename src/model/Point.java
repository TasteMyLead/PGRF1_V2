package model;

import java.awt.*;

public class Point extends Model {
    private final int x;
    private final int y;

    private Color color;

    public Point(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public Point(double x, double y, Color color) {
        this.x = (int) Math.round(x);
        this.y = (int) Math.round(y);
        this.color = color;
    }

    public Point(Point point,  Color color) {
        this.x = point.x;
        this.y = point.y;
        this.color = color;
    }



    public Point getPoint() {
        return new Point(this.x, this.y, this.color);
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public Color getColor() {return color;}

}
