/**
 * Sample Skeleton for 'Untitled' Controller Class
 */

package Project;

import Project.modules.Physics.Game;
import Project.modules.Physics.Stool;
import Project.modules.Test.TestParallel;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

enum Buttons{
    RESET(0),
    START(1),
    STOP(2);
    final public int ind;
    Buttons(int ind){
        this.ind = ind;
    }
}

public class PleaseProvideControllerClassName implements Initializable {
    List<PauseTransition> pauseTransitionList = new ArrayList<>();
    List<String> styleList = new ArrayList<>();
    private Game game;
    private Stool stool;

    @FXML
    private Button Def;

    @FXML
    private TextField DefField;

    @FXML
    private Button Set;

    @FXML
    private TextField SetField;

    @FXML // fx:id="Reset"
    private Button Reset; // Value injected by FXMLLoader

    @FXML // fx:id="SubScene"
    private SubScene SubScene; // Value injected by FXMLLoader

    @FXML // fx:id="graph"
    private LineChart<?, ?> graph; // Value injected by FXMLLoader

    @FXML // fx:id="start"
    private Button start; // Value injected by FXMLLoader

    @FXML // fx:id="stop"
    private Button stop; // Value injected by FXMLLoader

    @FXML
    void DefField(ActionEvent event) {
        System.out.println(SetField.getCharacters());
    }

    @FXML
    void SetAction(ActionEvent event) {

    }

    @FXML
    void DefAction(ActionEvent event) {

    }

    @FXML
    void StartClick(ActionEvent event) {
        game.setDEBUG(false);
        start.setStyle("-fx-background-color: rgba(125,132,132,0.37);");
        pauseTransitionList.get(Buttons.START.ind).playFromStart();
    }

    @FXML
    void StopClick(ActionEvent event) {

        game.setDEBUG(true);
        stop.setStyle("-fx-background-color: rgba(125,132,132,0.37);");
        pauseTransitionList.get(Buttons.STOP.ind).playFromStart();
    }

    @FXML
    void ResetClick(ActionEvent event) {
        game.setDEBUG(true);
        game = new Game();
        stool = new Stool(new Point2D(150,260),300,50,50,200,game, Color.AQUAMARINE);
        game.run(this.SubScene);
        game.initObjects();
        Reset.setStyle("-fx-background-color: rgba(125,132,132,0.37);");
        pauseTransitionList.get(Buttons.RESET.ind).playFromStart();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        long beginт, endT;
        beginт = System.nanoTime();

        TestParallel test = new TestParallel();
        for(int i = 0;i < 100;i++){
            test.addParam(i/100.0);
        }
        test.test();

        endT = System.nanoTime();

        System.out.println("Time : " + (endT - beginт) / 1000000000.0 + " с");

        game = new Game();
        new Stool(0.9,game,Color.AQUAMARINE);
        new Stool(0.4,game,Color.CORAL);
        game.initObjects();
        game.run(this.SubScene);
        this.SubScene.setFill(Color.WHITE);
        addButtonsPause();
    }

    private void addButtonsPause(){
        for(int i = 0;i < Buttons.values().length;i++) {
            pauseTransitionList.add(new PauseTransition(Duration.seconds(0.2)));
        }
        styleList.add(Reset.getStyle());
        styleList.add(start.getStyle());
        styleList.add(stop.getStyle());
        pauseTransitionList.get(Buttons.RESET.ind).setOnFinished(actionEvent -> Reset.setStyle(styleList.get(Buttons.RESET.ind)));
        pauseTransitionList.get(Buttons.START.ind).setOnFinished(actionEvent -> start.setStyle(styleList.get(Buttons.START.ind)));
        pauseTransitionList.get(Buttons.STOP.ind).setOnFinished(actionEvent -> stop.setStyle(styleList.get(Buttons.STOP.ind)));

    }
}
