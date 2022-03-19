package com.example.demo2;

import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;

import java.util.ArrayList;
import java.util.List;


class UserPair{
    private Point2D KEY1;
    private Point2D KEY2;
    private Point2D VALUE1;
    private Point2D VALUE2;
    UserPair(Point2D KEY, Point2D VALUE1, Point2D VALUE2){
        this.KEY1 = KEY;
        this.KEY2 = null;
        this.VALUE1 = VALUE1;
        this.VALUE2 = VALUE2;
    }
    UserPair(Point2D KEY1,Point2D KEY2, Point2D VALUE1, Point2D VALUE2){
        this.KEY1 = KEY1;
        this.KEY2 = KEY2;
        this.VALUE1 = VALUE1;
        this.VALUE2 = VALUE2;
    }

    public Point2D getVALUE2() {
        return VALUE2;
    }

    public void setVALUE2(Point2D VALUE2) {
        this.VALUE2 = VALUE2;
    }

    public Point2D getKEY1() {
        return KEY1;
    }

    public void setKEY1(Point2D KEY1) {
        this.KEY1 = KEY1;
    }

    public Point2D getVALUE1(){
        return VALUE1;
    }
    public void setVALUE1(Point2D VALUE1){
        this.VALUE1 = VALUE1;
    }

    public Point2D getKEY2() {
        return KEY2;
    }

    public void setKEY2(Point2D KEY2) {
        this.KEY2 = KEY2;
    }
}


class Vec2{
    private Point2D point_1;
    private Point2D point_2;
    private double a;
    private double b;
    private double c;
    Vec2(Point2D p1, Point2D p2){
        point_1 = p1;
        point_2 = p2;
    }
    Vec2(){
        point_1 = null;
        point_2 = null;
    }

    public void calculate(){
        a = 0;
        b = 0;
        c = 0;
        if(point_2.getX() == point_1.getX()){
            b = 0;
            a = 1;
            c = -point_2.getX();
        }else if (point_2.getY() == point_1.getY())
        {
            a = 0;
            b = 1;
            c = -point_2.getY();
        }else{
            a = 1 / (point_2.getX() - point_1.getX());
            c+= -point_1.getX()/(point_2.getX() - point_1.getX());
            b = -1 / (point_2.getY() - point_1.getY());
            c += point_1.getY()/(point_2.getY() - point_1.getY());
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


    public double lengthVec(){
        return Math.sqrt(Math.pow(point_2.getX() - point_1.getX(),2) + Math.pow(point_2.getY() - point_1.getY(),2));
    }
    public Point2D getDirection(){
        return new Point2D(point_2.getX() - point_1.getX(),point_2.getY() - point_1.getY());
    }

    public double DistanceVecPoint(Point2D point2D){
        return Math.abs(a* point2D.getX() +  b* point2D.getY() + c)/Math.sqrt(a*a + b*b);
    }

    Point2D NormalVec(Point2D point2D){
        double x0 = point2D.getX();
        double y0 = point2D.getY();
        double x = (b*(b*x0 - a*y0) - a*c)/(a*a + b*b);
        double y = (a*(-b*x0 + a*y0) - b*c)/(a*a + b*b);
        return new Point2D(x0 - x,y0 - y);
    }
}
class Block{
    public final Physics_Model physics_model;
    private final List<Point2D>normals = new ArrayList<>();
    Block(Rectangle rc, double m, Point2D V0){
        physics_model = new Physics_Model(rc,V0, m);
        normals.add(new Point2D(-1,0));
        normals.add(new Point2D(0,-1));
        normals.add(new Point2D(1,0));
        normals.add(new Point2D(0,1));
    }
    public List<Point2D> getNormals(){
        Transform transform = getRectangle().getLocalToParentTransform();
        List<Point2D> normals_ = this.normals;
        for(int i = 0;i < normals_.size();i++){
            normals_.set(i, transform.transform(normals_.get(i)).normalize());
        }
        return normals_;
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
    private static final long MOD = 100000000000L;

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

    static Point2D getPoint(Vec2 vec1, Vec2 vec2, Boolean stop1, Boolean stop2) {
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

        if(stop1 && (x < Math.min(x1_1, x1_2) ||  x > Math.max(x1_1, x1_2) || y < Math.min(y1_1, y1_2) || y > Math.max(y1_1, y1_2))){
            return null;
        }
        if(stop2 && (x < Math.min(x2_1, x2_2) || x > Math.max(x2_1, x2_2) || y < Math.min(y2_1, y2_2) ||y > Math.max(y2_1, y2_2) )){
            return null;
        }

        return new Point2D(x, y);

    }


    static double ROUND(double a){
        return a = (double)Math.round(a * MOD)/MOD;
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
     * Get rectangle points
     * */
    static List<Point2D> getPoints(Block block) {

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
        Vec2 vec2_1 = new Vec2(point1, point2);

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
     * return points of main (points are intersects block)
     * */
    static List<Point2D> IntersectsPoints(Block main, Block block){
        List<Point2D> main_points = getPoints(main);
        List<Point2D> point2DS = new ArrayList<>();
        for (Point2D main_point : main_points) {
            if (block.getRectangle().getBoundsInParent().contains(main_point)) {
                point2DS.add(main_point);
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


    /*
     * Get MAP :
     * " point rectangle intersects" : { points of intersects }
     *
     * */
    public static List<UserPair> getIntersectsPointListOnePoint(List<Point2D> ListIntersects,Block block){
        if( ListIntersects == null || ListIntersects.size() == 0){
            return null;
        }
        List<Point2D> Points = getPoints(block);
        List<UserPair> UserPairs = new ArrayList<>();
        for (Point2D point : Points) {
            for (int i1 = 0; i1 < ListIntersects.size(); i1++) {
                for (int i2 = i1 + 1; i2 < ListIntersects.size(); i2++) {
                    if (ROUND(ListIntersects.get(i1).subtract(point).dotProduct(ListIntersects.get(i2).subtract(point))) == 0) {
                        UserPairs.add(new UserPair(point,ListIntersects.get(i1),ListIntersects.get(i2)));
                    }
                }
            }
        }
        return UserPairs;

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
