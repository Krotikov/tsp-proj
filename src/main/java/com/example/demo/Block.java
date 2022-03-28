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

    public Point2D CenterBlock() {
        Rectangle rectangle = getRectangle();
        Transform transform = rectangle.getLocalToParentTransform();
        Point2D point1 = transform.transform(new Point2D(rectangle.getX(), rectangle.getY()));
        Point2D point2 = transform.transform(new Point2D(rectangle.getX() + rectangle.getWidth(), rectangle.getY() + rectangle.getHeight()));
        return point1.midpoint(point2);
    }

    public List<Point2D> getNormals() {
        List<Point2D> normals_ = new ArrayList<>(this.normals);

        // get rotate of block
        Rotate rotate = new Rotate(getRectangle().getRotate(), 0, 0);


        for (int i = 0; i < normals_.size(); i++) {
            normals_.set(i, rotate.transform(normals_.get(i)));
        }

        return normals_;
    }

    public Rectangle getRectangle() {
        return physics_model.getRectangle();
    }

    public void run(double t) {
        physics_model.run(t);
    }
}
