package csusm.cougarplanner;

//import csusm.cougarplanner.API;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class OrientationController implements Initializable {

    @FXML
    private TextField tokenField;

    @FXML
    private Button validateButton;

    @FXML
    private Label statusLabel;

    @FXML
    private CheckBox rememberTokenCheckBox;

    @FXML
    private CheckBox sundayCheckBox;

    @FXML
    private CheckBox mondayCheckBox;

        //cosmetic FXML interactives


        //if sundayCheckBox is selected, sunday status = true
        private static boolean sundayStatus = true; //you only need to know the selected status of sunday because the status of monday is implied

        //will flip the selected status of both the sunday and monday check boxes
        private void flip (boolean entry) {
            sundayStatus = !entry;
            sundayCheckBox.setSelected(sundayStatus);
            mondayCheckBox.setSelected(!sundayStatus);
        }

        @FXML
        private void weekBeginManager(MouseEvent event) {
            flip(sundayStatus);
        }

        @FXML
        private ScrollPane scrollPaneField; //field that controls the scroll feature; Vvalue will be changed

        @FXML
        private Label introductionLabel; //introduction label button on the left column of the screen

        @FXML
        private Label orientationLabel; //orientation label button on the left column of the screen

        @FXML
        private Label APITokenLabel; //API token label button on the left column of the screen

        @FXML
        private void introductionShortcut(MouseEvent event) {
            scrollPaneField.setVvalue(0.26);
        }

        @FXML
        private void orientationShortcut(MouseEvent event) {
            scrollPaneField.setVvalue(0.65);
        }

        @FXML
        private void APITokenShortcut(MouseEvent event) {
            scrollPaneField.setVvalue(0.93);
        }

    private Profile profileConfig;

    @FXML
    private void onValidateClicked(MouseEvent event) {
        String token = tokenField.getText().trim();
        if (token.isEmpty()) {
            statusLabel.setStyle("-fx-text-fill: #000000;");
            statusLabel.setText("|    Token cannot be empty.");
            return;
        }

        statusLabel.setStyle("-fx-text-fill: #000000;");
        statusLabel.setText("|    Validating token.");
        validateButton.setDisable(true);

        // Run token validation at the same time to avoid blocking the UI
        new Thread(() -> {
            boolean isValid = validateToken(token); // Using local method

            javafx.application.Platform.runLater(() -> {
                validateButton.setDisable(false);
                if (isValid) {
                    statusLabel.setStyle("-fx-text-fill: #02D541;");
                    statusLabel.setText("|    Token valid! Loading main view.");

                    //if (weekBeginningClass.getMondayCheckBox.isSelected()) {weekBegin = false;}

                    // Update ProfileConfig
                    profileConfig.setToken(token);
                    profileConfig.setOrientationCompleted(true);
                    profileConfig.setWeekStartingDay(sundayStatus ? "SUNDAY" : "MONDAY");
                    Launcher.updateProfileConfig(profileConfig);

                    // Switch to MainPage.fxml
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainPage.fxml"));
                        Parent root = loader.load();
                        Stage stage = (Stage) validateButton.getScene().getWindow();
                        stage.setScene(new Scene(root));
                        stage.setTitle("Cougar Planner - Weekly View");
                        stage.show();
                    } catch (Exception e) {
                        statusLabel.setStyle("-fx-text-fill: #ff3030;");
                        statusLabel.setText("|    Error loading main view.");
                        e.printStackTrace();
                    }

                } else {
                    statusLabel.setStyle("-fx-text-fill: #ff3030;");
                    statusLabel.setText("Invalid token. Please try again.");
                }
            });
        }).start();
    }

    /**
     * Skeleton token validation method.
     * Does not connect to Canvas API; always returns false.
     * Replace this with real API validation later.
     *
     * @param token The token string entered by the user
     * @return boolean indicating if the token is valid
     */
    private boolean validateToken(String token) {
        return false;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        profileConfig = Launcher.getProfileConfig(); // Load current user profile
        statusLabel.setText("Please enter your Canvas API token.");
        //rememberTokenCheckBox.setSelected(!profileConfig.getToken().isEmpty());
    }
}
