package model;

public class Edge extends Model {
    private int x1, y1, x2, y2;

    public Edge(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public Edge(Point p1, Point p2) {
        this.x1 = p1.getX();
        this.y1 = p1.getY();
        this.x2 = p2.getX();
        this.y2 = p2.getY();
    }

    public boolean isHorizontal() {
        return y1 == y2;
    }

    public void orientate() {
        if(y1 > y2) {
            int tmpy = y1;
            y1 = y2;
            y2 = tmpy;

            int tmpx = x1;
            x1 = x2;
            x2 = tmpx;
        }
    }

    public boolean isIntersection(int y) {
        return y1 <= y && y < y2;
    }

    public int getIntersection(int y) {
        if(isHorizontal()) return x1;

        double t = (double)(y - y1) / (y2 - y1);

        return(int)Math.round(x1 + t * (x2-x1));
    }

    public Point getIntersectionLine(Line line){
        double x3 = line.getX1(), y3 = line.getY1();
        double x4 = line.getX2(), y4 = line.getY2();

        double x1d = this.x1, y1d = this.y1, x2d = this.x2, y2d = this.y2;

        double denom = (x1d - x2d) * (y3 - y4) - (y1d - y2d) * (x3 - x4);

        if (denom == 0) {
            return null;
        }

        double t = ((x1d - x3) * (y3 - y4) - (y1d - y3) * (x3 - x4)) / denom;
        double u = ((x1d - x3) * (y1d - y2d) - (y1d - y3) * (x1d - x2d)) / denom;

        if (t >= 0 && t <= 1 && u >= 0 && u <= 1) {
            double ix = x1d + t * (x2d - x1d);
            double iy = y1d + t * (y2d - y1d);
            return new Point((int)Math.round(ix), (int)Math.round(iy),line.getStartPoint().getColor());
        }

        return null; // segmenty se nepřetínají
    }

    public int getX1() {
        return x1;
    }

    public int getX2() {
        return x2;
    }

    public int getY1() {
        return y1;
    }

    public int getY2() {
        return y2;
    }
}
