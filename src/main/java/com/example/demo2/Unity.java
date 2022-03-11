package com.example.demo2;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

enum BLOCKS{
    BLOCKS(0),
    PLATFORM(1);
    public final int id;

    BLOCKS(int id) {
        this.id = id;
    }
}


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
    final double TIMER = 0.1;

    public void unObjects(){
        /*
        * function for add objects
        * */


        // add block
        Block block = addBlock (0,0,50,50,40,new Point2D(20,0));

        // add platform
        Block platform = addBlock(0,SCENE_Y-300,5000,10,20,new Point2D(0,0));

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

        Block block = blocks.get(BLOCKS.BLOCKS.id);
        Block platform = blocks.get(BLOCKS.PLATFORM.id);

        // while of animation
        new AnimationTimer(){
            @Override
            public void handle(long l) {
                blocks.get(0).run(TIMER);
                Point2D point2 = Utility_Functions.intersects(block,platform);
                if(point2 != null) {
                    Circle circle = new Circle(point2.getX(),point2.getY(),2, Color.RED);
                    block.run(-TIMER);
                    block.physics_model.invY(-1);
                    group.getChildren().add(circle);
                    block.physics_model.stopPower();
                }else{
                    block.physics_model.startPower();
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
                    case UP -> block.physics_model.addY(-40);
                    case DOWN -> block.physics_model.addY(40);
                    case LEFT -> block.physics_model.addX(-40);
                    case RIGHT -> block.physics_model.addX(40);
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
                        block.physics_model.invX(0);
                        block.physics_model.invY(0);
                    }
                }
            }
        });
        group.getChildren().addAll(platform.getRectangle(),block.getRectangle(),text);
        stage.setScene(scene);
        stage.show();


    }
    private Block addBlock(double x,double y,double weight,double height,double mass, Point2D speed){
        Block block = new Block(new Rectangle(x,y,weight,height), mass, speed);
        blocks.add(block);
        return block;
    }

}
