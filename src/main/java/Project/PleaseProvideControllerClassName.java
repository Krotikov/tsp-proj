/**
 * Sample Skeleton for 'Untitled' Controller Class
 */

package Project;

import Project.modules.Physics.Camera;
import Project.modules.Physics.Game;
import Project.modules.Physics.Point;
import Project.modules.Physics.Stool;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SubScene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;

enum Buttons{
    RESET(0),
    START(1),
    STOP(2),
    DEF(3),
    SET(4);
    final public int ind;
    Buttons(int ind){
        this.ind = ind;
    }
}

public class PleaseProvideControllerClassName implements Initializable {
    List<PauseTransition> pauseTransitionList = new ArrayList<>();
    List<String> styleList = new ArrayList<>();
    Map<String,Stool> stoolMap = new HashMap<>();
    private Game game;
    private Stool stool;
    private Camera camera;

    @FXML
    private ListView<Label> Legend;

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
    void ListAction(MouseEvent event) {
        String key = Legend.getSelectionModel().getSelectedItems().get(0).getText();
        camera.bindPoint(ViewStoolPoint(stoolMap.get(key)));
    }

    @FXML
    void SetField(ActionEvent event) {
        Set.fire();
    }

    @FXML
    void DefField(ActionEvent event) {
        Def.fire();
    }

    @FXML
    void SetAction(ActionEvent event) {

        Set.setStyle("-fx-background-color: rgba(125,132,132,0.37); -fx-border-color: BLUE;");
        pauseTransitionList.get(Buttons.SET.ind).playFromStart();

        String num = SetField.getText();
        try {
            int val = Integer.parseInt(num);
            if(val > 100){
                SetField.setText(">100!");
            }
            if(val < 0){
                SetField.setText("<0!");
            }
        }catch (NumberFormatException e){
            SetField.setText("Wrong format ");
        }
    }

    @FXML
    void DefAction(ActionEvent event) {
        Def.setStyle("-fx-background-color: rgba(125,132,132,0.37); -fx-border-color: BLUE;");
        pauseTransitionList.get(Buttons.DEF.ind).playFromStart();

        String[] nums = DefField.getText().split(",");
        try {
            if(nums.length > 2){
                DefField.setText("too much numbers");
            }else {
                double val1 = Double.parseDouble(nums[0]);
                double val2 = Double.parseDouble(nums[1]);
                System.out.println(val1);
                System.out.println(val2);
            }
        }catch (ArrayIndexOutOfBoundsException e){
            DefField.setText("few numbers ");
        }catch (NumberFormatException e){
            DefField.setText("Wrong format");
        }

    }

    @FXML
    void StartClick(ActionEvent event) {
        game.setDEBUG(false);
        start.setStyle("-fx-background-color: rgba(125,132,132,0.37); -fx-border-color: BLUE;");
        pauseTransitionList.get(Buttons.START.ind).playFromStart();
    }

    @FXML
    void StopClick(ActionEvent event) {

        game.setDEBUG(true);
        stop.setStyle("-fx-background-color: rgba(125,132,132,0.37); -fx-border-color: BLUE;");
        pauseTransitionList.get(Buttons.STOP.ind).playFromStart();
    }

    @FXML
    void ResetClick(ActionEvent event) {
        game.setDEBUG(true);
        initAll();
        Reset.setStyle("-fx-background-color: rgba(125,132,132,0.37); -fx-border-color: BLUE;");
        pauseTransitionList.get(Buttons.RESET.ind).playFromStart();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        Label StoolOne = new Label("One");
        Label StoolTwo = new Label("two");

        Legend.setItems(FXCollections.observableArrayList(StoolOne, StoolTwo));
        Legend.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Label item, boolean empty) {
                super.updateItem(item, empty);
                if(item != null) {
                    setText(item.getText());
                    Color color =  stoolMap.get(item.getText()).getColor();
                    setStyle("-fx-background-color : rgb(" + color.getRed() *255 + ","
                            + color.getGreen() * 255 + "," + color.getBlue()*255 + ");");
                }
            }
        });
        initAll();

        this.SubScene.setFill(Color.WHITE);
        addButtonsPause();
    }

    private Point ViewStoolPoint(Stool stool){
        return stool.getBody().getAllPoints().get(0);
    }
    private void initAll(){

        stoolMap.clear();
        game = new Game();
        camera = new Camera(game);



        // add two Stools to project
        Stool stool1 = new Stool(1.20,game,Color.AQUAMARINE);
        stoolMap.put(Legend.getItems().get(0).getText(),stool1);
        Stool stool2 = new Stool(0.1,game,Color.BLANCHEDALMOND);
        stoolMap.put(Legend.getItems().get(1).getText(),stool2);


        game.initObjects();
        game.run(this.SubScene);
    }
    private void addButtonsPause(){
        for(int i = 0;i < Buttons.values().length;i++) {
            pauseTransitionList.add(new PauseTransition(Duration.seconds(0.2)));
        }
        styleList.add(Reset.getStyle());
        styleList.add(start.getStyle());
        styleList.add(stop.getStyle());
        styleList.add(Def.getStyle());
        styleList.add(Set.getStyle());
        pauseTransitionList.get(Buttons.RESET.ind).setOnFinished(actionEvent -> Reset.setStyle(styleList.get(Buttons.RESET.ind)));
        pauseTransitionList.get(Buttons.START.ind).setOnFinished(actionEvent -> start.setStyle(styleList.get(Buttons.START.ind)));
        pauseTransitionList.get(Buttons.STOP.ind).setOnFinished(actionEvent -> stop.setStyle(styleList.get(Buttons.STOP.ind)));
        pauseTransitionList.get(Buttons.DEF.ind).setOnFinished(actionEvent -> Def.setStyle(styleList.get(Buttons.DEF.ind)));
        pauseTransitionList.get(Buttons.SET.ind).setOnFinished(actionEvent -> Set.setStyle(styleList.get(Buttons.SET.ind)));

    }
}
