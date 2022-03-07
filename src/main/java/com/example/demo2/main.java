package com.example.demo2;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;

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
        ;
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
        final double TIMER = 0.1;

        //create scene
        stage.setTitle("APP");
        Group group = new Group();
        Scene scene = new Scene(group,SCENE_X,SCENE_Y);

        // create platform and blocks
        Block platform = new Block(new Rectangle(0,SCENE_Y - 100,5000,10),40);
        Block block = new Block(new Rectangle(0,0,50,50),40);
        platform.getRectangle().setRotate(0);


        // Text field
        Text text = new Text("FF");
        text.setFont(new Font(30));
        text.setX(SCENE_X * 0.5) ;
        text.setY(SCENE_Y - 50);



        new AnimationTimer(){
            @Override
            public void handle(long l) {
                block.run(TIMER);
                Point2D point2 = Utility_Functions.intersects(block,platform);
                if(point2 != null) {
                    Circle circle = new Circle(point2.getX(),point2.getY(),2, Color.RED);
                    System.out.print("y = ");
                    System.out.println(block.physics_model.V.getY());
                    if (block.physics_model.V.getY() != 0) {
                        block.run(-TIMER);
                    }
                    block.physics_model.inv_y(-1);
                    System.out.println(point2);
                    group.getChildren().add(circle);
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
