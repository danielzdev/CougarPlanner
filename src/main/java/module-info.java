module csusm.cougarplanner {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;

    opens csusm.cougarplanner to javafx.fxml;
    exports csusm.cougarplanner;
}
