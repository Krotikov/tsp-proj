package com.example.demo2;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Scanner;
import java.util.function.Consumer;

public class App extends Application {
    GraphicsContext gc;
    Color[] colors = {
            Color.RED, Color.BLUE,
            Color.GREEN, Color.BLACK
    };
    int colorIdx = 0;
    @Override
    public void start(Stage stage)  {
        stage.setTitle("Draw Directory to a Canvas");
        FlowPane rootNode = new FlowPane();

        rootNode.setAlignment(Pos.CENTER);

        Scene myScene = new Scene(rootNode, 450, 450);

        stage.setScene(myScene);

        Canvas canvas = new Canvas(400,400);

        gc = canvas.getGraphicsContext2D();

        Button btnChangeColor = new Button("Change Color");
        btnChangeColor.setOnAction(actionEvent ->{

            gc.setStroke(colors[colorIdx]);
            gc.setFill(colors[colorIdx]);

            gc.strokeLine(0,0,200,200);
            gc.fillText("This is dawn on the canvas.",60,50);

            gc.fillRect(100,320,300,40);
            colorIdx++;
            if(colorIdx == colors.length)colorIdx = 0;
        });
        gc.strokeLine(0,0,200,200);
        gc.strokeOval(100,100,200,200);
        gc.strokeRect(0,200,50,200);
        gc.fillOval(0 ,0,20, 20);
        gc.fillRect(100, 320, 300, 40);

        gc.setFont(new Font(20));
        gc.fillText("This is dawn on the canvas.",60,50);

        rootNode.getChildren().addAll(canvas, btnChangeColor);

        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}
