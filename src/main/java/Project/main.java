package Project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;


public class main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
//        Group group = new Group();
//        Scene scene = new Scene(group,900,900);
//        Unity unity = new Unity(600,600);
//        unity.unObjects();
//        SubScene subScene = unity.RUN();
//        group.getChildren().add(subScene);
//        stage.setScene(scene);
//        stage.show();
        FXMLLoader fxmlLoader = new FXMLLoader(PleaseProvideControllerClassName.class.getResource("template.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

        //Scene scene = new Scene(root, 300, 275);

        //stage.setTitle("FXML Welcome");
        //stage.setScene(scene);
        //stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}
