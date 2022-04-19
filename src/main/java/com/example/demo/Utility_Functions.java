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


    static double ROUND(double a){
        return (double)Math.round(a * MOD)/MOD;
    }

    static void bindBlocks(Block A, Block B){
        A.bindBlocks.add(B);
        B.bindBlocks.add(A);
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
    * return point of Center of block (center of mass)
    * */
    public static Point2D CenterRectangle(Rectangle rectangle){
        Transform transform = rectangle.getLocalToParentTransform();
        Point2D point1 = transform.transform(new Point2D(rectangle.getX(),rectangle.getY()));
        Point2D point2 = transform.transform(new Point2D(rectangle.getX() + rectangle.getWidth(),rectangle.getY() + rectangle.getHeight()));
        return point1.midpoint(point2);
    }


    public static Point2D Cross(double c, final Point2D v){
        return new Point2D(-c * v.getY(), c * v.getX());
    }
}
