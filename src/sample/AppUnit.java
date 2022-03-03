package sample;


import collision.utilityFunctions;
import entity.Block;
import entity.Polygon;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import math.Vec2;

import javafx.scene.canvas.Canvas;
import java.util.ArrayList;

public class AppUnit {
    private final ArrayList<Block> blockArray = new ArrayList<>();
    private final ArrayList<String> input = new ArrayList<>();
    private Canvas canvas;

    // Method where we can init all our app and entities
    public void OnUnit(Stage stage){
        Group root = new Group();
        Scene scene = new Scene(root);
        stage.setScene(scene);

        canvas = new Canvas(512, 512);
        root.getChildren().add(canvas);

        scene.setOnKeyPressed(keyEvent -> {
            String code = keyEvent.getCode().toString();

            if (!input.contains(code)){
                input.add(code);
            }
        }
        );

        scene.setOnKeyReleased(keyEvent -> {
            String code = keyEvent.getCode().toString();
            input.remove(code);
        });


        blockArray.add(new Block(new Vec2(50, 200), new Vec2(60, 60), Color.BLACK));
        blockArray.add(new Block(new Vec2(240, 200), new Vec2(60, 60), Color.BLACK));
        blockArray.add(new Block(new Vec2(300, 480), new Vec2(60, 60), Color.BLACK));
    }

    // App logic
    public void run(Stage stage){

        // Управляем только первым блоком
        new AnimationTimer()
        {
            @Override
            public void handle(long currentNanoTime){
                double dt = 1 / 30.0;

                moveSystem(dt);
                overlapSystem();
                drawSystem();
            }

        }.start();

        stage.show();
    }

    private void moveSystem(double dt){
        Polygon unit = blockArray.get(0).getPolygon();

        if (input.contains("LEFT")){
            unit.angle -= 2.0 * dt;
        }
        else if (input.contains("RIGHT")){
            unit.angle += 2.0 * dt;
        }
        else if (input.contains("UP")){
            unit.pos.setX(unit.pos.getX() + Math.cos(unit.angle) * 60 * dt);
            unit.pos.setY(unit.pos.getY() + Math.sin(unit.angle) * 60 * dt);
        }
        else if (input.contains("DOWN")){
            unit.pos.setX(unit.pos.getX() - Math.cos(unit.angle) * 60 * dt);
            unit.pos.setY(unit.pos.getY() - Math.sin(unit.angle) * 60 * dt);
        }

        for (Block block : blockArray){
            Polygon r = block.getPolygon();
            for (int i = 0; i < r.o.size(); i++){
                Vec2 vecRP = r.p.get(i);
                vecRP.setX((r.o.get(i).getX() * Math.cos(r.angle)) - (r.o.get(i).getY() * Math.sin(r.angle)) + r.pos.getX());
                vecRP.setY((r.o.get(i).getX() * Math.sin(r.angle)) + (r.o.get(i).getY() * Math.cos(r.angle)) + r.pos.getY());
            }

            r.overlap = false;
        }
    }

    private void overlapSystem(){
        // Проверяем пересечения (понадобится в будущем)
        for (int i = 0; i < blockArray.size(); i++){
            for (int j = i + 1; j < blockArray.size(); j++){
                blockArray.get(i).getPolygon().overlap
                        |= utilityFunctions.shapeOverlapDiag(blockArray.get(i).getPolygon(), blockArray.get(j).getPolygon());
            }
        }
    }

    private void drawSystem(){
        // Draw shapes
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.AQUAMARINE);

        for (Block block : blockArray){
            Polygon r = block.getPolygon();

            for (int i = 0; i < r.p.size(); i++){
                gc.strokeLine(
                        r.p.get(i).getX(),
                        r.p.get(i).getY(),
                        r.p.get((i + 1) % r.p.size()).getX(),
                        r.p.get((i + 1) % r.p.size()).getY()
                );
            }
        }
    }
}
