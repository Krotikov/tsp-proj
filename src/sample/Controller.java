package sample;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class Controller {

    @FXML
    private Button bNext;

    @FXML
    private Button bRestart;

    @FXML
    private Button bSet;

    @FXML
    private LineChart<?, ?> fPlot;

    @FXML
    private AnchorPane mainScreen;

    @FXML
    private MenuBar menuBar;

    @FXML
    private TextField textFiled;

    @FXML
    void initialize(){

    }

}
