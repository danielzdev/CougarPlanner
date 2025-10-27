package csusm.cougarplanner.controllers;

import csusm.cougarplanner.Launcher;
import csusm.cougarplanner.config.Profile;
import csusm.cougarplanner.config.ProfileWriter;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
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

    @FXML
    private ScrollPane scrollPaneField; //field that controls the scroll feature; Vvalue will be changed

    @FXML
    private Label introductionLabel; //introduction label button on the left column of the screen

    @FXML
    private Label orientationLabel; //orientation label button on the left column of the screen

    @FXML
    private Label APITokenLabel; //API token label button on the left column of the screen

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

        // Start measuring elapsed time
        long startTime = System.currentTimeMillis();

        // Run token validation at the same time to avoid blocking the UI
        new Thread(() -> {
            boolean isValid = validateToken(token); // Using local method
            long elapsed = System.currentTimeMillis() - startTime;

            // Ensure the total validation time is at least 1 second
            long delay = Math.max(0, 1300 - elapsed);

            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            javafx.application.Platform.runLater(() -> {
                validateButton.setDisable(false);
                if (isValid) {
                    sundayStatus = !mondayCheckBox.isSelected();

                    // Create new profile for the user
                    Profile profile = new Profile();
                    profile.setAuthToken(token);
                    profile.setOrientationCompleted(true);
                    profile.setWeekStart(sundayStatus ? "sunday" : "monday");

                    // Remember token if checkbox selected
                    profile.setStoreToken(rememberTokenCheckBox.isSelected());

                    // Set other defaults
                    profile.setDefaultView("week");
                    profile.setDefaultSortMode("date_time");
                    profile.setDefaultSortDifficultyOrder("ascending");

                    // Creating profile.properties file with given token with set defaults
                    try {
                        ProfileWriter writer = new ProfileWriter(Path.of("data/profile.properties"));
                        writer.writeProfile(profile);
                    } catch (IOException e) {
                        statusLabel.setStyle("-fx-text-fill: #ff3030;");
                        statusLabel.setText("|    Failed to save profile.");
                        e.printStackTrace();
                        return;
                    }

                    statusLabel.setStyle("-fx-text-fill: #02D541;");
                    statusLabel.setText("|    Token valid! Loading main view...");
                    try {
                        PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
                        pause.setOnFinished(ev -> {
                            try {
                                Launcher.loadScene("hello-view.fxml", "Cougar Planner - Weekly View");
                            } catch (Exception ex) {
                                statusLabel.setStyle("-fx-text-fill: #ff3030;");
                                statusLabel.setText("|    Failed to load Main Page.");
                                ex.printStackTrace();
                            }
                        });
                        pause.play();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    statusLabel.setStyle("-fx-text-fill: #ff3030;");
                    statusLabel.setText("Invalid token. Please try again.");
                }
            });
        }).start();
    }

    /**
     * Finished Validation method
     * Commented out for testing purposes
     *
     * @param token The token string entered by the user
     * @return boolean indicating if the token is valid
     */
    private boolean validateToken(String token) {
        /*try {
            API api = new API(); // Make sure API reads token from profile
            api.getCourses(); // Try fetching courses
            return true;
        } catch (Exception e) {
            return false;
        }*/
        return !token.isEmpty();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        statusLabel.setText("Please enter your Canvas API token.");
    }
}
