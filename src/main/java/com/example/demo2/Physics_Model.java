package com.example.demo2;

import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

import java.util.List;

class Physics_Model {

    // final.
    public final double g = 9.806; //9.806
    public final Point2D power_resistance = new Point2D(0, 0);

    // object.
    public Point2D velocity;
    public double wVelocity = 0;
    public final Rectangle rectangle; // have pos and angular

    public final double mass;

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
    public Point2D getVelocity() {
        return velocity;
    }

    /*
     * set speed
     * */
    public void setVelocity(Point2D new_v){
        velocity = new_v;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public Physics_Model(Rectangle rec, Point2D V1, double m) {
        mass = m;
        rectangle = rec;
        velocity = V1;
    }

    /*
     * get moment of inertia for rectangle
     * */
    private double getMomentOfInertia (){
        return mass*(rectangle.getWidth() * rectangle.getWidth()
                + rectangle.getHeight() *rectangle.getHeight()) /12;
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


    public double getAngularAcceleration(Point2D fulcrum){
        double coefficient = 10;
        Point2D R = Utility_Functions.CenterRectangle(rectangle).subtract(fulcrum);
        System.out.println(" ---- !!! !!!!  " + wVelocity);
        double w_new = (R.getX() * (power_resistance.getY() + g * mass) - R.getY()*power_resistance.getX())/(getMomentOfInertia()/coefficient);
        System.out.println(w_new);
        return w_new;
    }

    public void run(double t, List<Point2D> intersection) {

        double V_new_x = getSpeedX(t, velocity.getX(), mass);
        double x_0 = rectangle.getX();
        double x = x_0 + (V_new_x + velocity.getX()) * t / 2;
        rectangle.setX(x);

        double V_new_y = getSpeedY(t, velocity.getY(), mass);
        double y_0 = rectangle.getY();
        double y = y_0 + (V_new_y + velocity.getY()) * t / 2;
        rectangle.setY(y);
        velocity = new Point2D(V_new_x, V_new_y);


        // work with angle // work with angle // work with angle //
        t = Math.abs(t);
        if(intersection != null) {
            for (Point2D point2D : intersection) {
                if (point2D != null) {
                    double a_w = getAngularAcceleration(point2D) ;
                    double phi = a_w * (t * t) / 2 + wVelocity * t;
                    System.out.println("phi  = " + phi);
                    Utility_Functions.RotateOfPoint(point2D, rectangle, phi);
                    wVelocity += a_w * t;// new rotate speed
                    System.out.println(" --- w ---- : "  + wVelocity);

                }
            }
        }else{
            rectangle.setRotate(rectangle.getRotate() + wVelocity *t);
        }



    }

    public void invY(double k) {
        velocity = new Point2D(velocity.getX(), k * velocity.getY());
    }

    public void invX(double k) {
        velocity = new Point2D(k * velocity.getX(), velocity.getY());
    }

    public void addX(double x) {
        velocity = new Point2D(velocity.getX() + x, velocity.getY());
    }

    public void addY(double y) {
        velocity = new Point2D(velocity.getX(), velocity.getY() + y);
    }
}