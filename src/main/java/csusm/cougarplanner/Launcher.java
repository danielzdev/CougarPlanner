package csusm.cougarplanner;

import csusm.cougarplanner.config.Profile;
import csusm.cougarplanner.config.ProfileReader;
import csusm.cougarplanner.config.ProfileWriter;
import javafx.application.Application;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Launcher extends Application {

    private static Stage primaryStage;
    private static Profile profileConfig;
    private static ProfileStorage Storage = new ProfileStorage(); // Where we would load and save the data

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;

        // Load profile
        Profile profileConfig = Storage.loadProfile();

        // Decide which scene to load based on whether orientation is complete
        if (profileConfig.isOrientationCompleted() && !profileConfig.getToken().isEmpty()) {
            loadScene("MainPage.fxml", "Cougar Planner - Weekly View");
        } else {
            System.out.println("Debugged"); //Debug statement showing that the fxml file does not yet exist
            loadScene("Orientation.fxml", "Cougar Planner - Orientation");
        }
    }

    public static void loadScene(String fxmlPath, String title) throws Exception {
        FXMLLoader loader = new FXMLLoader(Launcher.class.getResource(fxmlPath));
        Scene scene = new Scene(loader.load());
        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static Profile getProfileConfig() {
        return profileConfig;
    }

    public static void updateProfileConfig(Profile newConfig) {
        profileConfig = newConfig;
        Storage.saveProfile(newConfig);
    }

    public static void main(String[] args) {
        launch();
    }
}
