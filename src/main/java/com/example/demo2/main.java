package com.example.demo2;


import javafx.application.Application;
import javafx.stage.Stage;


public class main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Unity unity = new Unity();
        unity.unObjects();
        unity.RUN(stage);
    }
    public static void main(String[] args) {;
        launch();
    }
}
