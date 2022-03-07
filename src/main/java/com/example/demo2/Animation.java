package com.example.demo2;

import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.lang.Math;



public class Animation extends Application{
    final float width = 700;
    final float weight = 600;
    final float R = 100;

    private static double new_value(double x_0,double x, double y_0 ){
        double a = 100*100;
        double b = Math.pow(x - x_0,2);
        return Math.pow((a - b),0.5) + y_0;
    }
    @Override
    public void start(Stage stage) {
        stage.setTitle("APP");
        Group root = new Group();
        Scene scene = new Scene(root,weight,width,Color.GREEN);
        stage.setScene(scene);

        Circle circle = new Circle( weight/2,width/2,50);
        Circle circle1 = new Circle(weight/2 - R,width/2,50,Color.BLUE);
        Circle circle2 = new Circle(weight/2 - 2*R,width/2, 50, Color.VIOLET);

        final long start_time = System.nanoTime();
        new AnimationTimer(){

            @Override
            public void handle(long l) {
                double t = (l - (double)start_time)/1000000000;
                double x,y;
                x = (R + 10)*Math.cos(t) + weight/2;
                y = (R + 250)*Math.sin(t) + width/2;
                circle1.setCenterX(x);
                circle1.setCenterY(y);
            }
        }.start();

        Path path = new Path();

        MoveTo moveTo = new MoveTo();
        moveTo.setX(100.0f);
        moveTo.setY(100.0f);

        HLineTo hLineTo = new HLineTo();
        hLineTo.setX(300.0f);

        ArcTo arcTo = new ArcTo();
        arcTo.setX(20f);
        arcTo.setY(320f);
        arcTo.setRadiusX(1f);
        arcTo.setRadiusY(1f);
        path.getElements().addAll(moveTo,hLineTo,arcTo);

        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(4000));
        pathTransition.setPath(path);
        pathTransition.setNode(circle2);
        pathTransition.setCycleCount(Timeline.INDEFINITE);
        pathTransition.setAutoReverse(true);

        pathTransition.play();


        //

        //KeyValue xValue = new KeyValue(rectangle.xProperty(),100);
        //KeyValue yValue = new KeyValue(rectangle.yProperty(),100);

        //KeyFrame keyFrame = new KeyFrame(Duration.millis(1000),xValue,yValue);

        //Timeline timeline = new Timeline();
        //timeline.setCycleCount(Timeline.INDEFINITE);
        //timeline.setAutoReverse(true);
        //timeline.getKeyFrames().addAll(keyFrame);
        //timeline.play();


        root.getChildren().addAll(circle,circle1,circle2);
        stage.show();





    }
    public static void main(String[] args) {
        launch();
    }

}
