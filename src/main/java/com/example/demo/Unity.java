package com.example.demo;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;

import java.nio.file.attribute.GroupPrincipal;
import java.util.ArrayList;
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
    Block platform = null;
    double TIMER = 1.0 / 30 ;

    Stool stool;
    Stool stool1;
    /*
     * function for add objects
     * */
    public void unObjects(){
        // add block
        platform = addBlock(-1000,SCENE_Y-200,5000,100,202000000,new Point2D(0,0), Color.BLACK);
        platform.physics_model.stopPower();
        Block block = addBlock (0,0,50,50,4,new Point2D(0,0), Color.AQUAMARINE);

        stool = new Stool(
                addBlock (200,700,400,50,10,new Point2D(0,0), Color.AQUAMARINE),
                addBlock (350,110,50,300,10,new Point2D(0,0), Color.BROWN),
                addBlock (100,110,50,300,10,new Point2D(0,0), Color.BROWN)
        );

        // add text
        Text text = new Text("FF");
        text.setFont(new Font(30));
        text.setX(SCENE_X * 0.5) ;
        text.setY(SCENE_Y - 50);
        shapes.add(text);
    }
    public void RUN(Stage stage)  {

        //create scene
        Group test = new Group();
        Group group = new Group();
        Group Main = new Group(test,group);
        test.toFront();
        Scene scene = new Scene(Main,SCENE_X,SCENE_Y);
        stage.setScene(scene);

        Block block = blocks.get(0);

        // while of animation
        // final long startNanoTime = System.nanoTime();


        new AnimationTimer(){
            long startNanoTime = System.nanoTime();
            double t;
            @Override
            public void handle(long currentNanoTime) {
                if(TIMER != 0) {
                    
                    // sorts blocks //
                    Utility_Functions.sortOSX(blocks);
                    Utility_Functions.sortOSY(blocks);
                    // sorts blocks //


                    t = (currentNanoTime - startNanoTime) / 1000000000.0;
                    startNanoTime = currentNanoTime;



                    for (int i = 0; i < blocks.size(); i++) {
                        if(blocks.get(i) == platform)continue;
                        blocks.get(i).run(t);
                        for (int j = 0; j < blocks.size(); j++) {
                            if (i == j) {
                                continue;
                            }

                            blocks.get(j).testRun(blocks.get(i));
                            if (Utility_Functions.IntersectsPoints(blocks.get(i), blocks.get(j)).size() > 0) {
                                Manifold manifold = new Manifold(blocks.get(i), blocks.get(j));
                                manifold.solveCollision();
                                if (manifold.isCollide) {
                                    manifold.applyImpulse();
                                    manifold.posCorr();
                                }else{
                                    blocks.get(j).testRun(blocks.get(i));
                                }
                            }



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

        // Mose event
        scene.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                Block bl = addBlock(mouseEvent.getX(), mouseEvent.getY(), 50, 50, 50, new Point2D(0, 0), Color.AQUAMARINE);
                group.getChildren().add(bl.getRectangle());
            }
        });
        // EVENT KEY
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()) {
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
                        System.out.println(transform.transform(block.getRectangle().getX(), block.getRectangle().getY()));
                        System.out.print(transform.transform(block.getRectangle().getX() + block.getRectangle().getWidth(), block.getRectangle().getY()));
                    }
                    case A -> {
                        Transform transform = block.getRectangle().getLocalToSceneTransform();
                        System.out.println(transform);
                    }
                    case SPACE -> {
                        block.physics_model.invX(0);
                        block.physics_model.invY(0);
                    }
                    case I -> {
                        Transform transform = block.getRectangle().getLocalToSceneTransform();
                        Point2D point2D = new Point2D(100, 100);

                        System.out.println(block.getRectangle().getTranslateX());

                    }
                    case C -> {
                        TIMER = 0;

                    }
                    case V -> {
                        TIMER = 1.0 / 30.0;
                    }
                }
            }
        });

        group.getChildren().add(text);
        for (Block block1 : blocks) {
            group.getChildren().add(block1.getRectangle());
        }
        block.getRectangle().toFront();
        stage.show();
    }
    private Block addBlock(double x, double y, double width, double height, double mass, Point2D speed, Paint paint, boolean add){
        Block block = new Block(new Rectangle(x,y,width,height), mass, speed);
        block.getRectangle().setFill(paint);
        if(add) {
            blocks.add(blocks.size() == 0 ? 0 : blocks.size() - 1, block);
        }
        return block;
    }
    private Block addBlock(double x, double y, double width, double height, double mass, Point2D speed, Paint paint){
        Block block = new Block(new Rectangle(x,y,width,height), mass, speed);
        block.getRectangle().setFill(paint);
        blocks.add(blocks.size() == 0 ? 0 : blocks.size() - 1, block);
        return block;
    }

}
