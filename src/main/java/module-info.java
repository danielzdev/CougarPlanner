module CougarPlanner {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.net.http;

    opens csusm.cougarplanner to javafx.fxml;
    exports csusm.cougarplanner;
    exports csusm.cougarplanner.controllers;
    opens csusm.cougarplanner.controllers to javafx.fxml;
}
