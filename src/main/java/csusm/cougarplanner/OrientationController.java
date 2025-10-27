package csusm.cougarplanner;

//import csusm.cougarplanner.API;
import csusm.cougarplanner.Launcher;
import csusm.cougarplanner.ProfileConfig;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.HashMap;
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
        private void flip(boolean entry) {
            sundayStatus = !entry;
            sundayCheckBox.setSelected(sundayStatus);
            mondayCheckBox.setSelected(!sundayStatus);
        }

        @FXML
        private void weekBeginManager(MouseEvent event) {
            flip(sundayStatus); //flips check status of both check boxes
        }

        @FXML
        private ScrollPane scrollPaneField; //field that controls the scroll feature; Vvalue will be changed

        @FXML
        private Label introductionLabel, orientationLabel, APITokenLabel;

        Label[] paneHitboxToLabelIndex;
        Double[] paneHitboxToScrollPercentageIndex = {0.26, 0.65, 0.93};

        @FXML
        private void shortcutTextHighlight(MouseEvent event) {
            if (event.getSource() instanceof Pane paneHitbox) {
                Label label = paneHitboxToLabelIndex[Integer.parseInt((String) paneHitbox.getUserData())];

                label.setStyle("-fx-text-fill: " + ((event.getEventType() == MouseEvent.MOUSE_ENTERED) ? "#ffe777" : "#ffffff"));
            }
        }

        @FXML
        private void scrollShortcut(MouseEvent event) {
            if (event.getSource() instanceof Pane hitboxPane) {
                int hitboxID = Integer.parseInt((String) hitboxPane.getUserData());

                scrollPaneField.setVvalue(paneHitboxToScrollPercentageIndex[hitboxID]);
            }
        }

        /*@FXML
        private void shortcutTextHighlight(MouseEvent event) {
            if (event.getSource() instanceof Pane paneHitbox){ //if the node that called shortcutTextHighlight is a pane, assign it to name paneHitbox
                AnchorPane parent = (AnchorPane) paneHitbox.getParent(); //find paneHitbox's parent and cast it to Anchor Pane type

                if (parent.lookup("#" + paneHitbox.getUserData()) instanceof Label label) { //if the parent has
                    //will change the color of the text based on weather the mouse is entering or exiting the respective label hitbox
                    label.setStyle("-fx-text-fill: " + ((event.getEventType() == MouseEvent.MOUSE_ENTERED) ? "ffe777" : "ffffff"));
                }
            }
        }*/

        /*@FXML
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
        }*/

    private ProfileConfig profileConfig;

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

        paneHitboxToLabelIndex = new Label[] {
                introductionLabel,
                orientationLabel,
                APITokenLabel
        };
    }
}
