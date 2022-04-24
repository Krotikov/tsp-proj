/**
 * Sample Skeleton for 'Untitled' Controller Class
 */

package Project;

import Project.modules.Physics.Unity;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;

public class PleaseProvideControllerClassName implements Initializable {

    private Unity unity;

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
    void StartClick(ActionEvent event) {
        unity.setDEBUG(false);
    }

    @FXML
    void StopClick(ActionEvent event) {
        unity.setDEBUG(true);
    }

    @FXML
    void ResetClick(ActionEvent event) {
        unity = new Unity();
        unity.unObjects();
        unity.RUN(this.SubScene);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        unity = new Unity();
        SubScene.setFill(Color.WHITE);
        unity.unObjects();
        unity.RUN(this.SubScene);
    }
}
