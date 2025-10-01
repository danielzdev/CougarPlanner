module csusm.cougarplanner {
    requires javafx.controls;
    requires javafx.fxml;


    opens csusm.cougarplanner to javafx.fxml;
    exports csusm.cougarplanner;
}