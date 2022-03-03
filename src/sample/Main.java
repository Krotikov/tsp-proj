package sample;


import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application
{
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception
    {
       // Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        stage.setTitle("Features");

        AppUnit appUnit = new AppUnit();
        appUnit.OnUnit(stage);
        appUnit.run(stage);
    }
}
