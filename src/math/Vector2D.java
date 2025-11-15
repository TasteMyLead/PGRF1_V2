package math;

public class Vector2D {
    private  double vx;
    private  double vy;


    public Vector2D(double x, double y) {
        this.vx = x;
        this.vy = y;
    }

    public double getX() {
        return vx;
    }
    public double getY() {
        return vy;
    }
    public void setX(double x) {
        this.vx = x;
    }
    public void setY(double y) {
        this.vy = y;
    }

    public double length() {
        return Math.sqrt(vx * vx + vy * vy);
    }

    public Vector2D normalOne(){
        return new Vector2D(-vy, vx);
    }
    public Vector2D normalTwo(){
        return new Vector2D(vy, -vx);
    }






}
