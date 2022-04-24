package Project.modules.Physics;

import javafx.geometry.Point2D;

public class Vec2 {
    private Point2D point_1;
    private Point2D point_2;
    private double a;
    private double b;
    private double c;

    Vec2(Point2D p1, Point2D p2) {
        point_1 = p1;
        point_2 = p2;
    }

    Vec2() {
        point_1 = null;
        point_2 = null;
    }

    public void calculate() {
        a = 0;
        b = 0;
        c = 0;
        if (point_2.getX() == point_1.getX()) {
            b = 0;
            a = 1;
            c = -point_2.getX();
        } else if (point_2.getY() == point_1.getY()) {
            a = 0;
            b = 1;
            c = -point_2.getY();
        } else {
            a = 1 / (point_2.getX() - point_1.getX());
            c += -point_1.getX() / (point_2.getX() - point_1.getX());
            b = -1 / (point_2.getY() - point_1.getY());
            c += point_1.getY() / (point_2.getY() - point_1.getY());
        }
    }

    public Point2D getPoint1() {
        return point_1;
    }

    public Point2D getPoint2() {
        return point_2;
    }

    public void setPoint1(Point2D point_1) {
        this.point_1 = point_1;
    }

    public void setPoint2(Point2D point_2) {
        this.point_2 = point_2;
    }


    public double lengthVec() {
        return Math.sqrt(Math.pow(point_2.getX() - point_1.getX(), 2) + Math.pow(point_2.getY() - point_1.getY(), 2));
    }

    public Point2D getDirection() {
        return new Point2D(point_2.getX() - point_1.getX(), point_2.getY() - point_1.getY());
    }

    public double DistanceVecPoint(Point2D point2D) {
        return Math.abs(a * point2D.getX() + b * point2D.getY() + c) / Math.sqrt(a * a + b * b);
    }

    Point2D NormalVec(Point2D point2D) {
        double x0 = point2D.getX();
        double y0 = point2D.getY();
        double x = (b * (b * x0 - a * y0) - a * c) / (a * a + b * b);
        double y = (a * (-b * x0 + a * y0) - b * c) / (a * a + b * b);
        return new Point2D(x0 - x, y0 - y);
    }
}
