package com.example.demo;

import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


class Utility_Functions {
    private static final List<Block> blocks = new ArrayList<>();
    private static final long MOD = 100000000000L;


    /*
    * comparators for os x and os y
    * */
    static final Comparator<Block> OSX = new Comparator<Block>() {
        @Override
        public int compare(Block one, Block two) {
            return Double.compare(one.getRectangle().getBoundsInParent().getMaxX(),two.getRectangle().getBoundsInParent().getMaxX());
        }
    };
    static final Comparator<Block> OSY = new Comparator<Block>() {
        @Override
        public int compare(Block one, Block two) {
            return Double.compare(one.getRectangle().getBoundsInParent().getMinY(),two.getRectangle().getBoundsInParent().getMinY());
        }
    };

    static void sortOSX(List<Block> blocks){
        blocks.sort(OSX);
    }

    static void sortOSY(List<Block> blocks){
        blocks.sort(OSY);
    }

    /*
     * point of intersect vector's
     * */
    static Point2D getPoint(Vec2 vec1, Vec2 vec2) {
        double x1_1 = vec1.getPoint1().getX();
        double x1_2 = vec1.getPoint2().getX();
        double y1_1 = vec1.getPoint1().getY();
        double y1_2 = vec1.getPoint2().getY();

        double x2_1 = vec2.getPoint1().getX();
        double x2_2 = vec2.getPoint2().getX();
        double y2_1 = vec2.getPoint1().getY();
        double y2_2 = vec2.getPoint2().getY();

        double x, y;

        if (x1_1 == x1_2) {
            x = x1_1;
            if (y2_1 == y2_2) {
                y = y2_1;
            } else {
                y = (x - x2_1) / (x2_2 - x2_1) * (y2_2 - y2_1) + y2_1;
            }
        } else if (x2_1 == x2_2) {
            x = x2_1;
            if (y1_1 == y1_2) {
                y = y1_1;
            } else {
                y = (x - x1_1) / (x1_2 - x1_1) * (y1_2 - y1_1) + y1_1;
            }
        } else if (y1_1 == y1_2) {
            y = y1_1;
            x = (y - y2_1) / (y2_2 - y2_1) * (x2_2 - x2_1) + x2_1;
        } else if (y2_1 == y2_2) {
            y = y2_1;
            x = (y - y1_1) / (y1_2 - y1_1) * (x1_2 - x1_1) + x1_1;
        } else {
            x = (x1_1 / (x1_2 - x1_1) * (y1_2 - y1_1) - x2_1 / (x2_2 - x2_1) * (y2_2 - y2_1) + y2_1 - y1_1) / ((y1_2 - y1_1) / (x1_2 - x1_1) - (y2_2 - y2_1) / (x2_2 - x2_1));
            y = (x - x1_1) / (x1_2 - x1_1) * (y1_2 - y1_1) + y1_1;
        }


        if (x < Math.min(x1_1, x1_2) || x < Math.min(x2_1, x2_2) || x > Math.max(x2_1, x2_2) || x > Math.max(x1_1, x1_2)) {
            return null;
        }
        if (y < Math.min(y1_1, y1_2) || y < Math.min(y2_1, y2_2) || y > Math.max(y2_1, y2_2) || y > Math.max(y1_1, y1_2)) {
            return null;
        }

        return new Point2D(x, y);

    }



    static double ROUND(double a){
        return a = (double)Math.round(a * MOD)/MOD;
    }

    static void bindBlocks(Block A, Block B){
        A.bindBlocks.add(B);
        B.bindBlocks.add(A);
    }

    /*
     * return: array of rectangle's vector components
     * add_sides - add two additional vectors or not
     * */
    static List<Vec2> getVectors(Block block) {

        List<Point2D> points = block.getPoints();
        Transform transform = block.getRectangle().getLocalToParentTransform();

        // левый верхний угол и правый верхний угол
        // верхняя линия
        Vec2 vec2_1 = new Vec2(points.get(0), points.get(1));

        // левый нижний угол и правый нижний угол
        // верхняя линия
        Vec2 vec2_2 = new Vec2(points.get(3), points.get(2));

        ArrayList<Vec2> vec2s = new ArrayList<>();
        vec2s.add(vec2_1);
        vec2s.add(vec2_2);

        {
            vec2s.add(new Vec2(vec2_1.getPoint2(), vec2_2.getPoint2()));
            vec2s.add(new Vec2(vec2_1.getPoint1(), vec2_2.getPoint1()));
        }


        return vec2s;
    }

    /*
     * return points of main (points are intersects block)
     * */
    static List<Point2D> IntersectsPoints(Block block1, Block block2){
        List<Point2D> block1Points = block1.getPoints();
        List<Point2D> block2Points = block2.getPoints();
        List<Point2D> point2DS = new ArrayList<>();
        for (Point2D block1_point : block1Points) {
            if (block2.contains(block1_point)) {
                point2DS.add(block1_point);
            }
        }
        for(Point2D block2_point : block2Points){
            if(block1.contains(block2_point)){
                point2DS.add(block2_point);
            }
        }
        return point2DS;
    }



    /*
     * return Point of intersects block1 and block2
     * */
    static List<Point2D> intersects(Block block1, Block block2) {
        List<Point2D> point2DS = new ArrayList<>();

        List<Vec2> vec2List_block1 = getVectors(block1);
        List<Vec2> vec2List_block2 = getVectors(block2);
        Point2D point2D;
        if (block1.getRectangle().getBoundsInParent().intersects(block2.getRectangle().getBoundsInParent())) {
            for (Vec2 vec2 : vec2List_block1) {
                for (Vec2 vec21 : vec2List_block2) {
                    point2D = getPoint(vec2, vec21);
                    if (point2D != null) {
                        point2DS.add(point2D);
                    }
                }
            }
        }

        block1.physics_model.contacts = point2DS;
        return point2DS;
    }


    public static Point2D getPointFunc(Point2D e, Point2D point2D){
        if(e.getX() != 0 && e.getY() != 0) {
            double k = e.getY() / e.getX();
            double a = 1;
            double b = -k;
            double c = k * point2D.getX() - point2D.getY();
            double x = point2D.getX() - 20;
            double y = (-c - a*x)/b;
            return new Point2D(x,y);
        }
        if(e.getY() != 0){
            double y = point2D.getY() - 20;
            return new Point2D(point2D.getX(),y);
        }
        if(e.getX() != 0){
            double x = point2D.getX() - 20;
            return new Point2D(x, point2D.getY());
        }
        return null;
    }

    /*
    * return point of Center of block (center of mass)
    * */
    public static Point2D CenterRectangle(Rectangle rectangle){
        Transform transform = rectangle.getLocalToParentTransform();
        Point2D point1 = transform.transform(new Point2D(rectangle.getX(),rectangle.getY()));
        Point2D point2 = transform.transform(new Point2D(rectangle.getX() + rectangle.getWidth(),rectangle.getY() + rectangle.getHeight()));
        return point1.midpoint(point2);
    }

    public static void RotateOfPoint(Point2D point2, Rectangle rectangle, double phi){
        Rotate rotate = new Rotate();
        double y_center = rectangle.getBoundsInParent().getCenterY();
        double x_center = rectangle.getBoundsInParent().getCenterX();

      //  rectangle.getBoundsInParent().getDepth();

        rotate.setPivotY(point2.getY());
        rotate.setPivotX(point2.getX());
        rotate.setAngle(phi);

        Rotate rotate1 = new Rotate();
        rotate1.setPivotX(x_center);
        rotate1.setPivotY(y_center);
        rotate1.setAngle(phi);

        Point2D new_p = rotate1.transform(new Point2D(point2.getX(), point2.getY()));
        Point2D del_p = point2.subtract(new_p);

        Point2D start = new Point2D(rectangle.getX(),rectangle.getY());

        start = start.add(del_p);

        rectangle.setRotate(phi + rectangle.getRotate());

        rectangle.setX(start.getX());
        rectangle.setY(start.getY());
    }

    public static Point2D Cross(double c, final Point2D v){
        return new Point2D(-c * v.getY(), c * v.getX());
    }
}
