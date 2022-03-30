package com.example.demo;

import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;

import java.util.ArrayList;
import java.util.List;

public class Block {
    public final Physics_Model physics_model;
    private final List<Point2D> normals = new ArrayList<>();

    Block(Rectangle rc, double m, Point2D V0) {
        physics_model = new Physics_Model(rc, V0, m);
        normals.add(new Point2D(0, -1));
        normals.add(new Point2D(1, 0));
        normals.add(new Point2D(0, 1));
        normals.add(new Point2D(-1, 0));
    }

    /*
     * Get rectangle points
     * */
    List<Point2D> getPoints() {
        Block block = this;

        Transform transform = block.getRectangle().getLocalToParentTransform();

        List<Point2D> Points = new ArrayList<>();

        // левый верхний угол и правый верхний угол
        Point2D point1 = new Point2D(block.getRectangle().getX(), block.getRectangle().getY());
        Point2D point2 = new Point2D(block.getRectangle().getX() + block.getRectangle().getWidth(), block.getRectangle().getY());
        point1 = transform.transform(point1);
        point2 = transform.transform(point2);


        // верхняя линия
        Points.add(point1);
        Points.add(point2);

        // левый нижний угол и правый нижний угол
        point1 = new Point2D(block.getRectangle().getX(), block.getRectangle().getY() + block.getRectangle().getHeight());
        point2 = new Point2D(block.getRectangle().getX() + block.getRectangle().getWidth(), block.getRectangle().getY() + block.getRectangle().getHeight());
        point1 = transform.transform(point1);
        point2 = transform.transform(point2);

        // верхняя линия
        Points.add(point2);
        Points.add(point1);

        return Points;
    }

    /*
    * contains point or not
    * */
    public boolean contains(Point2D point){
        List<Point2D> corners = this.getPoints();

        // sides of the rectangle
        Point2D one = corners.get(1).subtract(corners.get(0));
        Point2D two = corners.get(2).subtract(corners.get(1));
        Point2D three = corners.get(3).subtract(corners.get(2));
        Point2D four = corners.get(0).subtract(corners.get(3));

        // vector from point to sides
        Point2D one_point = point.subtract(corners.get(0));
        Point2D two_point = point.subtract(corners.get(1));
        Point2D three_point = point.subtract(corners.get(2));
        Point2D four_point = point.subtract(corners.get(3));

        //calc the dot product
        boolean sca_one = one.dotProduct(one_point) >= 0;
        boolean sca_two = two.dotProduct(two_point) >= 0;
        boolean sca_three = three.dotProduct(three_point) >= 0;
        boolean sca_four = four.dotProduct(four_point) >= 0;

        if(sca_one && sca_two && sca_three && sca_four){
            return true;
        }else return !(sca_one | sca_two | sca_three | sca_four);
    }


    /*
    * get point of center of block
    * */
    public Point2D CenterBlock() {
        Rectangle rectangle = getRectangle();
        Transform transform = rectangle.getLocalToParentTransform();
        Point2D point1 = transform.transform(new Point2D(rectangle.getX(), rectangle.getY()));
        Point2D point2 = transform.transform(new Point2D(rectangle.getX() + rectangle.getWidth(), rectangle.getY() + rectangle.getHeight()));
        return point1.midpoint(point2);
    }

    /*
    * get array of block normals
    * */
    public List<Point2D> getNormals() {
        List<Point2D> normals_ = new ArrayList<>(this.normals);

        // get rotate of block
        Rotate rotate = new Rotate(getRectangle().getRotate(), 0, 0);


        for (int i = 0; i < normals_.size(); i++) {
            normals_.set(i, rotate.transform(normals_.get(i)));
        }

        return normals_;
    }

    /*
    * get rectangle object of block
    * */
    public Rectangle getRectangle() {
        return physics_model.getRectangle();
    }

    public void run(double t) {
        physics_model.run(t);
    }
}
