package com.example.demo2;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

enum SHAPES{
    TEXT(0);
    public final int id;
    SHAPES(int id) {
        this.id = id;
    }
}

public class Unity {
    final private List<Block> blocks = new ArrayList<>();
    final private List<Shape> shapes = new ArrayList<>();
    final double SCENE_X = 1000;
    final double SCENE_Y = 600;
    double TIMER = 0.01;

    /*
     * function for add objects
     * */
    public void unObjects(){
        // add block
        Block block = addBlock (0,0,150,150,500,new Point2D(20,0), Color.AQUAMARINE);
        Block block2 = addBlock (250,100,150,150,500,new Point2D(0,0), Color.AQUAMARINE);
        // add platform
        Block platform = addBlock(0,SCENE_Y-300,5000,30,20,new Point2D(0,0), Color.BLACK);

        // add text
        Text text = new Text("FF");
        text.setFont(new Font(30));
        text.setX(SCENE_X * 0.5) ;
        text.setY(SCENE_Y - 50);
        shapes.add(text);


    }
    public void RUN(Stage stage)  {

        //create scene
        Group group = new Group();
        Scene scene = new Scene(group,SCENE_X,SCENE_Y);

       Block block = blocks.get(0);

        // while of animation
        new AnimationTimer(){
            @Override
            public void handle(long l) {
                for (int i = 0; i < blocks.size() - 1; i++) {
                    for (int j = i + 1; j < blocks.size(); j++) {

                        List<Point2D> point2 = Utility_Functions.intersects(blocks.get(i), blocks.get(j));

                        Block block1 = blocks.get(i);
                        block1.run(TIMER, point2);
                        for (Point2D point2D : point2) {
                            System.out.println(point2D);
                        }
                        if (point2.size() != 0) {
                            block1.run(-TIMER, point2);
                            for (Point2D point2D : point2) {
                                Circle circle = new Circle(point2D.getX(), point2D.getY(), 5, Color.RED);
                                group.getChildren().addAll(circle);

                                System.out.println(point2D);
                            }
                            block1.physics_model.invY(-0.3);

                        }
                    }
                }
            }
        }.start();

        // for testing /// // // for testing // /// //
        Text text = (Text) shapes.get(SHAPES.TEXT.id);
        scene.setOnMouseMoved(event -> {
            String msg =
                    "x: " +event.getX()      + ", y: "       + event.getY()        ;
            text.setText(msg);
        });

        // EVENT KEY
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()){
                    case UP: block.physics_model.addY(-40);
                    break;
                    case DOWN: block.physics_model.addY(40);
                    break;
                    case LEFT: block.physics_model.addX(-40);
                    break;
                    case RIGHT:block.physics_model.addX(40);
                    break;
                    case R: {
                        block.getRectangle().setScaleX(block.getRectangle().getScaleX() + 10);
                        break;
                    }
                    case Q: {
                        block.getRectangle().setScaleX(block.getRectangle().getScaleX() - 10);
                        break;
                    }
                    case U: {
                        block.getRectangle().setRotate(block.getRectangle().getRotate() + 10);
                        break;

                    }
                    case O: {
                        Transform transform = block.getRectangle().getLocalToParentTransform();
                        System.out.println(transform);
                        System.out.println(block.getRectangle().getX());
                        System.out.println(block.getRectangle().getX() + block.getRectangle().getWidth());
                        System.out.println(transform.transform(block.getRectangle().getX(),block.getRectangle().getY()));
                        System.out.print(transform.transform(block.getRectangle().getX() + block.getRectangle().getWidth(),block.getRectangle().getY()));
                        break;
                    }
                    case A: {
                        Transform transform = block.getRectangle().getLocalToSceneTransform();
                        System.out.println(transform);
                        break;
                    }
                    case SPACE: {
                        block.physics_model.invX(0);
                        block.physics_model.invY(0);
                        break;
                    }
                    case I: {
                        Transform transform = block.getRectangle().getLocalToSceneTransform();
                        Point2D point2D = new Point2D(100,100);
                        System.out.println(block.getRectangle().getTranslateX());
                        break;

                    }
                    case C: {
                        TIMER = 0;
                    }
                    case V: {
                        TIMER = 0.1;
                    }

                }
            }
        });

        group.getChildren().add(text);
        for (Block block1 : blocks) {
            group.getChildren().add(block1.getRectangle());
        }
        stage.setScene(scene);
        stage.show();
    }
    private Block addBlock(double x, double y, double width, double height, double mass, Point2D speed, Paint paint){
        Block block = new Block(new Rectangle(x,y,width,height), mass, speed);
        block.getRectangle().setFill(paint);
        blocks.add(block);
        return block;
    }

}
