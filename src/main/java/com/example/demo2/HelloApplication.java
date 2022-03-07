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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Scanner;
import java.util.function.Consumer;

public class HelloApplication extends Application {
    Label response;
    @Override
    public void start(Stage stage)  {
        stage.setTitle("JavaFX");

        FlowPane rootNode = new FlowPane(10,10);
        rootNode.setAlignment(Pos.CENTER);

        Scene scene = new Scene(rootNode, 300,300);
        stage.setScene(scene);

        response = new Label("This is a JavaFx label");

        Button btnAlpha = new Button("Alpha");
        Button btnBeta = new Button("Beta");
        Canvas canvas = new Canvas(100,100);
        canvas.getGraphicsContext2D().setFill(Color.GREEN);
        canvas.getGraphicsContext2D().fillRect(1,1,100,50);

        btnAlpha.setOnAction(actionEvent -> response.setText("Alpha"));
        btnBeta.setOnAction(actionEvent -> response.setText("Beta"));


        rootNode.getChildren().addAll(btnAlpha,btnBeta,response,canvas);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
@FunctionalInterface
interface Fm{
    double number(double x);
}