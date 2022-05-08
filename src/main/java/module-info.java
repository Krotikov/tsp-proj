module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires lombok;

    opens Project.modules.Physics to javafx.fxml;
    exports Project.modules.Physics;
    exports Project;
    opens Project to javafx.fxml;
}