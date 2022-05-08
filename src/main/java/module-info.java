module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires lombok;

    exports Project.modules.Physics;
    exports Project;
    opens Project to javafx.fxml;
}