package com.example.demo2;

import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

class Physics_Model {

    // final.
    public final double g = 9.806; //9.806
    public final Point2D power_resistance = new Point2D(10, 120);

    // object.
    private final double mass;
    private final Rectangle rectangle;
    private Point2D V;
    private boolean stop = false;


    public double getSpeedY(double t, double v0, double m) {
        /*
        * t[IN]: time
        * v0[IN]: start speed
        * m[IN]: mass of object
        * V[OUT]: total speed
        * returns the speed on the y-axis (including forces)
        * */
        double a = 0;
        if (!stop) {
            a = (g * m - Math.signum(v0) * power_resistance.getY()) / m;
            return a * t + v0;
        }
        return v0;
    }

    public double getSpeedX(double t, double v0, double m) {
        /*
         * t[IN]: time
         * v0[IN]: start speed
         * m[IN]: mass of object
         * V[OUT]: total speed
         * returns the speed on the x-axis (including forces)
         * */
        double a = 0;
        if (!stop) {
            a = power_resistance.getX() / m;
            return a * t + v0;
        }
        return v0;
    }

    public Point2D getV() {
        /*
        * V[OUT]: vector speed
        * */
        return V;
    }

    public void setV(Point2D new_v){
        /*
        *
        * */
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

    public void addPower(Point2D pow) {
        power_resistance.add(pow);
    }

    public void stopPower() {
        /*
        * Uniform movement
        * */
        stop = true;
    }

    public void startPower() {
        /*
        * Uniformly accelerated motion
        * */
        stop = false;
    }

    public void run(double t) {
        /*
        * function for changes position of object (include powers or not)
        * t[IN]: time
        * */

        double V_new_x = getSpeedX(t, V.getX(), mass);
        double x_0 = rectangle.getX();
        double x = x_0 + (V_new_x + V.getX()) * t / 2;
        rectangle.setX(x);

        double V_new_y = getSpeedY(t, V.getY(), mass);
        double y_0 = rectangle.getY();
        double y = y_0 + (V_new_y + V.getY()) * t / 2;
        rectangle.setY(y);
        V = new Point2D(V_new_x, V_new_y);
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
