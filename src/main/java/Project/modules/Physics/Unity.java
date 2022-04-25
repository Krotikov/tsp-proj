package Project.modules.Physics;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.ParallelCamera;
import javafx.scene.SubScene;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Transform;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

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
    double SCENE_X;
    double SCENE_Y;
    Set<Block> platforms = new HashSet<>();
    boolean DEBUG = false;
    boolean View = false;

    Stool stool;

    /*
     * function for add objects
     * */
    public void unObjects(){
        Block platforms1 = addBlock(-2000,SCENE_Y + 500 ,10000,100,202000000,new Point2D(0,0), Color.BLACK);
        platforms1.physics_model.setAngle(0);
        platforms.add(platforms1);


        Block block = addBlock (0,0,50,50,40,new Point2D(0,0), Color.AQUAMARINE);

        stool = new Stool(
                addBlock (100,160,200,50,50,new Point2D(0,0), Color.AQUAMARINE), // body
                addBlock (100,210,20,200,50,new Point2D(0,0), Color.BROWN),      // leftLeg
                addBlock (280,210,20,200,50,new Point2D(0,0), Color.BROWN)       // RightLeg
        );


        // add text
        Text text = new Text("FF");
        text.setFont(new Font(30));
        text.setX(SCENE_X * 0.5) ;
        text.setY(SCENE_Y - 50);
        shapes.add(text);
    }

    public void setDEBUG(boolean DEBUG) {
        this.DEBUG = DEBUG;
    }

    public SubScene RUN(SubScene scene)  {
        Camera camera = new ParallelCamera();
        SCENE_X = scene.getWidth();
        SCENE_Y = scene.getHeight();
        scene.setCamera(camera);

        //create scene
        Group test = new Group();
        Group group = new Group();
        group.getChildren().add(camera);
        Group Main = new Group(test,group);
        scene.setRoot(Main);
        test.toFront();

        Block block = blocks.get(0);

        // while of animation
        // final long startNanoTime = System.nanoTime();


        new AnimationTimer(){
            long startNanoTime = System.nanoTime();
            double t;
            @Override
            public void handle(long currentNanoTime) {
                if(!DEBUG) {
                    
                    // sorts blocks //
                    Utility_Functions.sortOSX(blocks);
                    Utility_Functions.sortOSY(blocks);
                    // sorts blocks //


                    t = (currentNanoTime - startNanoTime) / 1000000000.0;
                    startNanoTime = currentNanoTime;

                    // fix time
                    t = 1./45;
                        stool.AlphaRun2();
                        for (int i = 0; i < blocks.size(); i++) {
                            if (platforms.contains(blocks.get(i))) continue;

                            // run objects
                            blocks.get(i).run(t);
                            for (int j = 0; j < blocks.size(); j++) {
                                if (i == j) {
                                    continue;
                                }
                                if (Utility_Functions.IntersectsPoints(blocks.get(i), blocks.get(j)).size() > 0) {
                                    Manifold manifold = new Manifold(blocks.get(i), blocks.get(j));
                                    manifold.solveCollision();
                                    if (manifold.isCollide) {
                                        manifold.applyImpulse();
                                        manifold.posCorr();
                                    }
                                }
                            }
                            blocks.get(i).testRun(null);
                        }


                }
            }
        }.start();



        // for test // for test //
        AtomicReference<Point2D> translate = new AtomicReference<>(new Point2D(0, 0));

        AtomicReference<Double> MouseX = new AtomicReference<>(0.0);
        AtomicReference<Double> MouseY = new AtomicReference<>( 0.0);

        AtomicReference<Double> oldMouseX = new AtomicReference<>(0.0);
        AtomicReference<Double> oldMouseY = new AtomicReference<>(0.0);
        // for testing /// // // for testing // /// //
        Text text = (Text) shapes.get(SHAPES.TEXT.id);
        scene.setOnMouseMoved(event -> {
            String msg =
                    "x: " +event.getX()      + ", y: "       + event.getY()        ;
            text.setText(msg);
            if(View){
                MouseX.set(event.getX());
                MouseY.set(event.getY());
                camera.setTranslateX(oldMouseX.get() - MouseX.get() + translate.get().getX());
                camera.setTranslateY(oldMouseY.get() - MouseY.get() + translate.get().getY());
            }
        });



        // Mose event
        scene.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                System.out.println(mouseEvent.getX());
                Block bl = addBlock(mouseEvent.getX() + translate.get().getX(), mouseEvent.getY() + translate.get().getY(), 50, 50, 50, new Point2D(0, 0), Color.AQUAMARINE);
                group.getChildren().add(bl.getRectangle());

            }
            if(mouseEvent.getButton() == MouseButton.SECONDARY){
                View = !View;
                if(!View){
                    translate.set(new Point2D(oldMouseX.get() - MouseX.get() + translate.get().getX(), oldMouseY.get() - MouseY.get() + translate.get().getY()));
                }
                oldMouseX.set(mouseEvent.getX());
                oldMouseY.set(mouseEvent.getY());
            }
        });
        // EVENT KEY
        scene.setOnKeyPressed(keyEvent -> {
            switch (keyEvent.getCode()) {
//                case UP -> block.physics_model.addY(-40);
//                case DOWN -> block.physics_model.addY(40);
//                case LEFT -> block.physics_model.addX(-40);
//                case RIGHT -> block.physics_model.addX(40);
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
                case RIGHT -> {
                    camera.setTranslateX(100);
                }
                case LEFT -> {
                    camera.setTranslateX(-100);
                }

            }
        });

        group.getChildren().add(text);
        for (Block block1 : blocks) {
            group.getChildren().add(block1.getRectangle());
        }
        block.getRectangle().toFront();
        return scene;
    }

    private Block addBlock(double x, double y, double width, double height, double mass, Point2D speed, Paint paint){
        Block block = new Block(new Rectangle(x,y,width,height), mass, speed);
        block.getRectangle().setFill(paint);
        blocks.add(blocks.size() == 0 ? 0 : blocks.size() - 1, block);
        return block;
    }

}
