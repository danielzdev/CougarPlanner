package csusm.cougarplanner;

import csusm.cougarplanner.config.Profile;
import csusm.cougarplanner.config.ProfileReader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.nio.file.Path;

public class Launcher extends Application {

    private static Stage primaryStage;
    private static Stage secondaryStage;
    private static Profile profileConfig;
    private static final Path PROFILE_PATH = Path.of("data/profile.properties");

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;

        // Load profile
        ProfileReader reader = new ProfileReader(PROFILE_PATH);
        ProfileReader.ReadResult result = reader.readProfile();
        profileConfig = result.getProfile();

        // Decide which scene to load based on whether orientation is complete
        if (profileConfig.isOrientationCompleted() && !profileConfig.getAuthToken().isEmpty()) {
            loadScene("hello-view.fxml", "Cougar Planner - Weekly View");
        } else {
            loadScene("Orientation.fxml", "Cougar Planner - Orientation");
        }
    }

    public static void loadScene(String fxmlPath, String title) throws Exception {
        FXMLLoader loader = new FXMLLoader(Launcher.class.getResource(fxmlPath));
        Scene scene = new Scene(loader.load());

        //if primaryStage's scene is null, loadScene is being called from Start()
        if (primaryStage.getScene() == null) {
            if (title.equals("Cougar Planner - Weekly View")) {
                primaryStage.initStyle(StageStyle.TRANSPARENT);
                scene.setFill(Color.TRANSPARENT);
            }
            primaryStage.setTitle(title);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        else { //if primaryStage's scene is not null, loadScene() is being called from a controller and a new stage needs to be launched

            // If primaryStage is titled Orientation, loadScene() is being called from the orientation controller
            if (primaryStage.getTitle().equals("Cougar Planner - Orientation")) {
                secondaryStage = new Stage();
                secondaryStage.initStyle(StageStyle.TRANSPARENT);
                scene.setFill(Color.TRANSPARENT);

                secondaryStage.setTitle(title);
                secondaryStage.setScene(scene);
                secondaryStage.show();
            }
            else { //currently does nothing - will handle other controllers and stages
                ;
            }
        }
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static Stage getCurrentStage() {
        return (primaryStage.isShowing() ? primaryStage : secondaryStage);
    }

    public static void main(String[] args) {
        launch();
    }
}
