package com.example.demo2;

import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

import java.util.List;

class Physics_Model {

    // final.
    public final double g = 9.806; //9.806
    public final Point2D power_resistance = new Point2D(0, 0);

    // object.
    private double w = 0;
    private final double mass;
    private final Rectangle rectangle;
    private Point2D V;
    private boolean stop = false;


    /*
     * t[IN]: time
     * v0[IN]: start speed
     * m[IN]: mass of object
     * V[OUT]: total speed
     * returns the speed on the y-axis (including forces)
     * */
    public double getSpeedY(double t, double v0, double m) {
        double a = 0;
        if (!stop) {
            a = (g * m - Math.signum(v0) * power_resistance.getY()) / m;
            return a * t + v0;
        }
        return v0;
    }

    /*
     * t[IN]: time
     * v0[IN]: start speed
     * m[IN]: mass of object
     * V[OUT]: total speed
     * returns the speed on the x-axis (including forces)
     * */
    public double getSpeedX(double t, double v0, double m) {
        double a = 0;
        if (!stop) {
            a = power_resistance.getX() / m;
            return a * t + v0;
        }
        return v0;
    }

    /*
     * V[OUT]: vector speed
     * */
    public Point2D getV() {
        return V;
    }

    /*
     * set speed
     * */
    public void setV(Point2D new_v){
        V = new_v;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public Physics_Model(Rectangle rec, Point2D V1, double m) {
        mass = m;
        rectangle = rec;
        V = V1;
    }

    /*
     * get moment of inertia for rectangle
     * */
    private double getMomentOfInertia (){
        return mass*(rectangle.getWidth() * rectangle.getWidth()
                + rectangle.getHeight() *rectangle.getHeight()) /12;
    }


    public double getW(){
        return w;
    }


    public void addPower(Point2D pow) {
        power_resistance.add(pow);
    }

    /*
     * Uniform movement
     * */
    public void stopPower() {
        stop = true;
    }

    /*
     * Uniformly accelerated motion
     * */
    public void startPower() {
        stop = false;
    }

    /*
     * function for changes position of object (include powers or not)
     * t[IN]: time
     * */
    public double getAngularAcceleration(Point2D point2D){
        double coefficient = 10;
        Point2D R = Utility_Functions.CenterRectangle(rectangle).subtract(point2D);
        System.out.println(" ---- !!! !!!!  " + w);
        double w_new = (R.getX() * (power_resistance.getY() + g * mass) - R.getY()*power_resistance.getX())/(getMomentOfInertia()/coefficient);
        System.out.println(w_new);
        return w_new;
    }
    public void rot(){
        rectangle.setRotate(rectangle.getRotate() + w);
    }

    public void run(double t, List<Point2D> intersection) {

        double V_new_x = getSpeedX(t, V.getX(), mass);
        double x_0 = rectangle.getX();
        double x = x_0 + (V_new_x + V.getX()) * t / 2;
        rectangle.setX(x);

        double V_new_y = getSpeedY(t, V.getY(), mass);
        double y_0 = rectangle.getY();
        double y = y_0 + (V_new_y + V.getY()) * t / 2;
        rectangle.setY(y);
        V = new Point2D(V_new_x, V_new_y);


        // work with angle // work with angle // work with angle //
        t = Math.abs(t);
        if(intersection != null) {
            for (Point2D point2D : intersection) {
                if (point2D != null) {
                    double a_w = getAngularAcceleration(point2D) ;
                    double phi = a_w * (t * t) / 2 + w * t;
                    System.out.println("phi  = " + phi);
                    Utility_Functions.RotateOfPoint(point2D, rectangle, phi);
                    w += a_w * t;// new rotate speed
                    System.out.println(" --- w ---- : "  +w);

                }
            }
        }else{
            rectangle.setRotate(rectangle.getRotate() + w*t);
        }



    }

    public void invY(double k) {
        V = new Point2D(V.getX(), k * V.getY());
    }

    public void invX(double k) {
        V = new Point2D(k * V.getX(), V.getY());
    }

    public void addX(double x) {
        V = new Point2D(V.getX() + x, V.getY());
    }

    public void addY(double y) {
        V = new Point2D(V.getX(), V.getY() + y);
    }
}
