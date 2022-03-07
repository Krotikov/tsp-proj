package com.example.demo2;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;


class Utility_Functions{
    private static final List<Block> blocks = new ArrayList<>();


    static Point2D get_point(Vec2 vec1, Vec2 vec2){
        /*
        * point of intersect vector's
        * can go beyond the segments !!!
        * */
        double x1_1 = vec1.getPoint_1().getX();
        double x1_2 = vec1.getPoint_2().getX();
        double y1_1 = vec1.getPoint_1().getY();
        double y1_2 = vec1.getPoint_2().getY();

        double x2_1 = vec2.getPoint_1().getX();
        double x2_2 = vec2.getPoint_2().getX();
        double y2_1 = vec2.getPoint_1().getY();
        double y2_2 = vec2.getPoint_2().getY();

        double x,y;

        if(x1_1 == x1_2){
            x = x1_1;
            if(y2_1 == y2_2){
                y = y2_1;
            }else{
                y = (x - x2_1)/(x2_2 - x2_1) * (y2_2 - y2_1) + y2_1;
            }
        }else if (x2_1 == x2_2){
            x = x2_1;
            if(y1_1 == y1_2){
                y = y1_1;
            }else{
                y = (x - x1_1)/(x1_2 - x1_1) * (y1_2 - y1_1) + y1_1;
            }
        }else if (y1_1 == y1_2){
            y = y1_1;
            x = (y - y2_1)/(y2_2 - y2_1) * (x2_2 - x2_1) + x2_1;
        }else if (y2_1 == y2_2){
            y = y2_1;
            x = (y - y1_1)/(y1_2 - y1_1) * (x1_2 - x1_1) + x1_1;
        }else{
            x = (x1_1/(x1_2 - x1_1) * (y1_2 - y1_1) - x2_1/(x2_2 - x2_1) * (y2_2 - y2_1) + y2_1 - y1_1)/((y1_2 - y1_1)/(x1_2 - x1_1) - (y2_2 - y2_1)/(x2_2 - x2_1));
            y = (x - x1_1)/(x1_2 - x1_1) * (y1_2 - y1_1) + y1_1;
        }


        if (x < Math.min(x1_1,x1_2) || x < Math.min(x2_1,x2_2) || x > Math.max(x2_1,x2_2) || x > Math.max(x1_1,x1_2)){
            return null;
        }

        return new Point2D(x,y);

    }
    static List<Vec2> get_vectors(Block block){
        /*
        * return: array of rectangle's vector components
        * add_sides - add two additional vectors or not
        * */

        Transform transform = block.getRectangle().getLocalToParentTransform();

        // левый верхний угол и правый верхний угол
        Point2D point1 = new Point2D(block.getRectangle().getX(), block.getRectangle().getY());
        Point2D point2 = new Point2D(block.getRectangle().getX() + block.getRectangle().getWidth(),block.getRectangle().getY());
        point1 = transform.transform(point1);
        point2 = transform.transform(point2);

        // верхняя линия
        Vec2 vec2_1 = new Vec2(point1,point2);

        // левый нижний угол и правый нижний угол
        point1 = new Point2D(block.getRectangle().getX(), block.getRectangle().getY() + block.getRectangle().getHeight());
        point2 = new Point2D(block.getRectangle().getX() + block.getRectangle().getWidth(),block.getRectangle().getY() + block.getRectangle().getHeight());
        point1 = transform.transform(point1);
        point2 = transform.transform(point2);

        // верхняя линия
        Vec2 vec2_2 = new Vec2(point1,point2);

        ArrayList<Vec2> vec2s = new ArrayList<>();
        vec2s.add(vec2_1);
        vec2s.add(vec2_2);

        if (true){
            vec2s.add(new Vec2(vec2_1.getPoint_2(),vec2_2.getPoint_2()));
            vec2s.add(new Vec2(vec2_1.getPoint_1(),vec2_2.getPoint_1()));
        }


        return vec2s;
    }
    static Point2D intersects(Block block){
        List<Vec2> vec2List_block = get_vectors(block);
        Point2D point2D;
        for (Block block_inter : blocks){
            if(block.getRectangle().getBoundsInParent().intersects(block_inter.getRectangle().getBoundsInLocal())){
                List<Vec2> vec2List_block_inter = get_vectors(block_inter);
                for(Vec2 vec2 : vec2List_block){
                    for(Vec2 vec21: vec2List_block_inter){
                        point2D = get_point(vec2,vec21);
                        if(point2D != null){
                            return point2D;
                        }
                    }
                }
            }
        }
        return null;
    }
    static Point2D intersects(Block block1,Block block2){
        List<Vec2> vec2List_block1 = get_vectors(block1);
        List<Vec2> vec2List_block2 = get_vectors(block2);
        Point2D point2D;
        if(block1.getRectangle().getBoundsInParent().intersects(block2.getRectangle().getBoundsInParent())){
            for(Vec2 vec2 : vec2List_block1){
                for(Vec2 vec21: vec2List_block2){
                    point2D = get_point(vec2,vec21);
                    if(point2D != null){
                        return point2D;
                    }
                }
            }
        }
        return null;
    }
    static void add_block(Block block){
        blocks.add(block);
    }
}


class Physics_Model{

    // static.
    public final static double g = 9.806; //9.806
    public  final static Point2D power_resistance = new Point2D(0.001,0);

    // object.
    private final double mass;
    private final Rectangle rectangle;
    public Point2D V;
    private static boolean stop = false;


    public static double get_speed_y(double t,double v0,double m){
        double a = 0;
        if(!stop) {
            a = (g * m - Math.signum(v0) * power_resistance.getY()) / m;
            return a*t + v0;
        }
        return 0;
    }
    public static double get_speed_x(double t,double v0, double m){
        double a = 0;
        if(!stop) {
            a = power_resistance.getX() / m;
            return a*t + v0;
        }
        return 0;
    }
    public Point2D getV(){
        return V;
    }
    public Rectangle get_rectangle(){
        return rectangle;
    }
    public Physics_Model(Rectangle rec, Point2D V1, double m){
        mass = m;
        rectangle = rec;
        V = V1;
    }
    public void add_power(Point2D pow){
        power_resistance.add(pow);
    }
    public void stop_power(){
        stop = true;
    }
    public void start_power(){
        stop = false;
    }
    public void run(double t){

        double V_new_x = get_speed_x(t,V.getX(),mass);
        double x_0 = rectangle.getX();
        double x = x_0 + (V_new_x + V.getX())*t/2;
        rectangle.setX(x);

        double V_new_y = get_speed_y(t,V.getY(),mass);
        double y_0 = rectangle.getY();
        double y = y_0 + (V_new_y + V.getY())*t/2;
        rectangle.setY(y);
        V = new Point2D(V_new_x,V_new_y);
    }
    public void inv_y(double k){
        V = new Point2D(V.getX(),k*V.getY());
    }
    public void inv_x(double k){
        V = new Point2D(k*V.getX(),V.getY());
    }
    public void add_x(double x){
        V = new Point2D(V.getX() + x,V.getY());
    }
    public void add_y(double y){
        V = new Point2D(V.getX(),V.getY() + y);
    }
}
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

    public Point2D getPoint_1() {
        return point_1;
    }

    public Point2D getPoint_2() {
        return point_2;
    }

    public void setPoint_1(Point2D point_1) {
        this.point_1 = point_1;
    }

    public void setPoint_2(Point2D point_2) {
        this.point_2 = point_2;
    }
}

class Block{
    public final Physics_Model physics_model;
    Block(Rectangle rc, double m){
        physics_model = new Physics_Model(rc,new Point2D(5,0), m);
    }
    public Rectangle getRectangle() {
        return physics_model.get_rectangle();
    }
    public void add_power(Point2D power){

    }
    public void run(double t){
        physics_model.run(t);
    }
}


public class App_ver1 extends Application {


    @Override
    public void start(Stage stage) {
        final int SCENE_X = 1000;
        final int SCENE_Y = 600;

        stage.setTitle("APP");
        Group group = new Group();
        Scene scene = new Scene(group,SCENE_X,SCENE_Y);

        Block platform = new Block(new Rectangle(0,SCENE_Y - 100,5000,10),40);
        Block block = new Block(new Rectangle(0,0,50,50),40);

        Text text = new Text("FF");
        text.setFont(new Font(30));
        text.setX(SCENE_X * 0.5) ;
        text.setY(SCENE_Y - 50);

        new AnimationTimer(){
            @Override
            public void handle(long l) {
                block.run(0.2);
                Point2D point2 = Utility_Functions.intersects(block,platform);
                if(point2 != null) {
                    System.out.print("y = ");
                    System.out.println(block.physics_model.V.getY());
                    if (block.physics_model.V.getY() != 0) {
                        block.run(-0.2);
                    }
                    block.physics_model.inv_y(-0.7);
                    System.out.println(point2);
                }else{
                    block.physics_model.start_power();
                }
                double x = block.getRectangle().getBoundsInLocal().getMaxX();
            }
        }.start();


        scene.setOnMouseMoved(event -> {
            String msg =
                    "(x: "       + event.getX()      + ", y: "       + event.getY()       + ") -- " ;
            text.setText(msg);
        });

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()){
                    case UP -> block.physics_model.add_y(-40);
                    case DOWN -> block.physics_model.add_y(40);
                    case LEFT -> block.physics_model.add_x(-40);
                    case RIGHT -> block.physics_model.add_x(40);
                    case R -> {
                        block.getRectangle().setScaleX(block.getRectangle().getScaleX() + 10);
                            }
                    case Q -> {
                        block.getRectangle().setScaleX(block.getRectangle().getScaleX() - 10);
                    }
                    case U -> {
                        block.getRectangle().setRotate(block.getRectangle().getRotate() + 10);

                    }
                    case O -> {
                        Transform transform = block.getRectangle().getLocalToParentTransform();
                        System.out.println(transform);
                        System.out.println(block.getRectangle().getX());
                        System.out.println(block.getRectangle().getX() + block.getRectangle().getWidth());
                        System.out.println(transform.transform(block.getRectangle().getX(),block.getRectangle().getY()));
                        System.out.print(transform.transform(block.getRectangle().getX() + block.getRectangle().getWidth(),block.getRectangle().getY()));
                    }
                    case A -> {
                        Transform transform = block.getRectangle().getLocalToSceneTransform();
                    }
                    case SPACE -> {
                        block.physics_model.inv_x(0);
                        block.physics_model.inv_y(0);
                    }
                }
            }
        });


        group.getChildren().addAll(platform.getRectangle(),block.getRectangle(),text);
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}
