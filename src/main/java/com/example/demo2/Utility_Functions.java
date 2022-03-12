package com.example.demo2;

import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Transform;

import java.util.ArrayList;
import java.util.List;



class Vec2{
    private Point2D point_1;
    private Point2D point_2;
    Vec2(Point2D p1, Point2D p2){
        point_1 = p1;
        point_2 = p2;
    }
    Vec2(){
        point_1 = null;
        point_2 = null;
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
}
class Block{
    public final Physics_Model physics_model;
    Block(Rectangle rc, double m, Point2D V0){
        physics_model = new Physics_Model(rc,V0, m);
    }
    public Rectangle getRectangle() {
        return physics_model.getRectangle();
    }
    public void run(double t){
        physics_model.run(t);
    }
}




class Utility_Functions {
    private static final List<Block> blocks = new ArrayList<>();


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

    /*
     * return: array of rectangle's vector components
     * add_sides - add two additional vectors or not
     * */
    static List<Vec2> getVectors(Block block) {

        Transform transform = block.getRectangle().getLocalToParentTransform();

        // левый верхний угол и правый верхний угол
        Point2D point1 = new Point2D(block.getRectangle().getX(), block.getRectangle().getY());
        Point2D point2 = new Point2D(block.getRectangle().getX() + block.getRectangle().getWidth(), block.getRectangle().getY());
        point1 = transform.transform(point1);
        point2 = transform.transform(point2);


        // верхняя линия
        Vec2 vec2_1 = new Vec2(point1, point2);

        // левый нижний угол и правый нижний угол
        point1 = new Point2D(block.getRectangle().getX(), block.getRectangle().getY() + block.getRectangle().getHeight());
        point2 = new Point2D(block.getRectangle().getX() + block.getRectangle().getWidth(), block.getRectangle().getY() + block.getRectangle().getHeight());
        point1 = transform.transform(point1);
        point2 = transform.transform(point2);

        // верхняя линия
        Vec2 vec2_2 = new Vec2(point1, point2);

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
     * return Point of intersects block1 and block2
     * */
    static Point2D intersects(Block block1, Block block2) {
        List<Vec2> vec2List_block1 = getVectors(block1);
        List<Vec2> vec2List_block2 = getVectors(block2);
        Point2D point2D;
        if (block1.getRectangle().getBoundsInParent().intersects(block2.getRectangle().getBoundsInParent())) {
            for (Vec2 vec2 : vec2List_block1) {
                for (Vec2 vec21 : vec2List_block2) {
                    point2D = getPoint(vec2, vec21);
                    if (point2D != null) {
                        return point2D;
                    }
                }
            }
        }
        return null;
    }
}
