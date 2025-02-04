module it.unibo.smartcrossroads {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens it.unibo.smartcrossroads to javafx.fxml;
    exports it.unibo.smartcrossroads;
}