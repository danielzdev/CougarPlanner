package csusm.cougarplanner;

import csusm.cougarplanner.config.Profile;
import csusm.cougarplanner.config.ProfileReader;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.StageStyle;

import java.net.URL;
import java.nio.file.Path;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    ProfileReader reader = new ProfileReader(Path.of("data/profile.properties"));
    Profile profile = reader.readProfile().getProfile();

    @FXML
    private AnchorPane viewingMenu;

    @FXML
    private Pane viewingHitbox;

    @FXML
    private AnchorPane plannerBody;

    boolean viewingMenuIsOpen = false;

    @FXML
    private void toggleOrderMenu(MouseEvent event) {
        viewingMenu.setVisible(!viewingMenuIsOpen);
        viewingHitbox.setVisible(!viewingMenuIsOpen);

        viewingMenuIsOpen = !viewingMenuIsOpen;

        plannerBody.setEffect(viewingMenuIsOpen ? new BoxBlur() : null);
        viewingMenu.setOpacity(1.0);
    }

    @FXML
    private void fuzzPlannerBody(MouseEvent event) {
        plannerBody.setEffect((event.getEventType() == MouseEvent.MOUSE_ENTERED) ? null : new BoxBlur());
        viewingMenu.setOpacity((event.getEventType() == MouseEvent.MOUSE_ENTERED) ? 0.25 : 1.0);
    }

    @FXML
    private void highlightFromText(MouseEvent event) {
        if (event.getSource() instanceof Label label) {
            label.setStyle("-fx-text-fill: " + ((event.getEventType() == MouseEvent.MOUSE_ENTERED) ? "#ffe777" : "#ffffff"));
        }
    }

    @FXML
    private Label orderMenuLabel;

    @FXML
    private Rectangle announcementsRectangle, assignmentsRectangle;

    boolean showAnnouncements = true; //false - show assignments

    @FXML
    private void toggleContentsType(MouseEvent event) {
        if (event.getSource() instanceof Label label) {
            boolean userClickedAnnouncements = label.getText().equals("Announcements");

            if (showAnnouncements != userClickedAnnouncements) {
                orderMenuLabel.setText(userClickedAnnouncements ? "Announcements" : "Assignments");
                announcementsRectangle.setVisible(userClickedAnnouncements);
                assignmentsRectangle.setVisible(!userClickedAnnouncements);

                showAnnouncements = userClickedAnnouncements;
            }
        }
    }

    @FXML
    private Label headerLabel;

    @FXML
    private Rectangle weekRectangle, dayRectangle;

    @FXML
    private AnchorPane weekPlanner, dayPlanner;

    boolean defaultView = profile.getDefaultView().equals("week"); //true - week ; false - day

    @FXML
    private void toggleViewByWeek(MouseEvent event) {
        if (event.getSource() instanceof Label label) {
            boolean userClickedViewByWeek = label.getText().equals("Week");

            if (defaultView != userClickedViewByWeek) {
                String headerText = headerLabel.getText();
                String headerText1 = headerText.substring(0,4);
                String headerText2 = headerText.substring(defaultView ? 8 : 7);

                String textToBeAdded = defaultView ? "day" : "week";

                headerLabel.setText(headerText1 + textToBeAdded + headerText2);

                weekRectangle.setVisible(userClickedViewByWeek);
                dayRectangle.setVisible(!userClickedViewByWeek);

                weekPlanner.setVisible(userClickedViewByWeek);
                dayPlanner.setVisible(!userClickedViewByWeek);

                defaultView = userClickedViewByWeek;
                profile.setDefaultView(defaultView ? "week" : "day");

                organizePlannerByWeekStart(); //the first day of the week isn't changed on the day-week view that's hidden, so it needs to be updated
            }
        }
    }

    @FXML
    private Rectangle sundayRectangle, mondayRectangle;

    boolean weekStart = profile.getWeekStart().equals("sunday"); //true - sunday ; false - monday

    @FXML
    private void toggleWeekStart(MouseEvent event) {
        if (event.getSource() instanceof Label label) {
            boolean userClickedSunday = label.getText().equals("Sunday");

            if (weekStart != userClickedSunday) { //if the user clicks the other unselected option
                sundayRectangle.setVisible(userClickedSunday);
                mondayRectangle.setVisible(!userClickedSunday);

                weekStart = userClickedSunday;
                profile.setWeekStart(weekStart ? "sunday" : "monday");

                organizePlannerByWeekStart();
            }
        }
    }

    @FXML
    private HBox hboxWeekHeaderContainer;

    @FXML
    private AnchorPane sundayHeaderPane, mondayHeaderPane, tuesdayHeaderPane, wednesdayHeaderPane, thursdayHeaderPane, fridayHeaderPane, saturdayHeaderPane;

    @FXML
    private HBox hboxWeekContentsContainer;

    @FXML
    private AnchorPane sundayContentsPane, mondayContentsPane, tuesdayContentsPane, wednesdayContentsPane, thursdayContentsPane, fridayContentsPane, saturdayContentsPane;

    @FXML
    private HBox hboxDayHeaderContainer;

    @FXML
    private AnchorPane sundayDayHeaderPane, mondayDayHeaderPane, tuesdayDayHeaderPane, wednesdayDayHeaderPane, thursdayDayHeaderPane, fridayDayHeaderPane, saturdayDayHeaderPane;


    private boolean firstDayOfWeekWeekTracker = true; //true - sunday, false - monday
    private boolean firstDayOfWeekDayTracker = true; //true - sunday, false - monday

    private void organizePlannerByWeekStart() {
        boolean newWeekStart = profile.getWeekStart().equals("sunday");

        if (defaultView) { //if user is currently seeing planner week view
            if (newWeekStart == firstDayOfWeekWeekTracker) {
                return; //return if no change is necessary
            }
            if (newWeekStart) { //change the week beginning to sunday
                hboxWeekHeaderContainer.getChildren().removeAll(mondayHeaderPane, tuesdayHeaderPane, wednesdayHeaderPane, thursdayHeaderPane, fridayHeaderPane, saturdayHeaderPane);
                hboxWeekHeaderContainer.getChildren().addAll(mondayHeaderPane, tuesdayHeaderPane, wednesdayHeaderPane, thursdayHeaderPane, fridayHeaderPane, saturdayHeaderPane);
                hboxWeekContentsContainer.getChildren().removeAll(mondayContentsPane, tuesdayContentsPane, wednesdayContentsPane, thursdayContentsPane, fridayContentsPane, saturdayContentsPane);
                hboxWeekContentsContainer.getChildren().addAll(mondayContentsPane, tuesdayContentsPane, wednesdayContentsPane, thursdayContentsPane, fridayContentsPane, saturdayContentsPane);
                firstDayOfWeekWeekTracker = true;
            }
            else { //change the week beginning to monday
                hboxWeekHeaderContainer.getChildren().remove(0);
                hboxWeekHeaderContainer.getChildren().add(sundayHeaderPane);
                hboxWeekContentsContainer.getChildren().remove(0);
                hboxWeekContentsContainer.getChildren().add(sundayContentsPane);
                firstDayOfWeekWeekTracker = false;
            }
        }
        else { //if user is currently seeing planner day view
            if (newWeekStart == firstDayOfWeekDayTracker) {
                return; //return if no change is necessary
            }
            if (newWeekStart) { //change the week beginning to sunday
                hboxDayHeaderContainer.getChildren().removeAll(mondayDayHeaderPane, tuesdayDayHeaderPane, wednesdayDayHeaderPane, thursdayDayHeaderPane, fridayDayHeaderPane, saturdayDayHeaderPane);
                hboxDayHeaderContainer.getChildren().addAll(mondayDayHeaderPane, tuesdayDayHeaderPane, wednesdayDayHeaderPane, thursdayDayHeaderPane, fridayDayHeaderPane, saturdayDayHeaderPane);
                firstDayOfWeekDayTracker = true;
            }
            else { //change the week beginning to monday
                hboxDayHeaderContainer.getChildren().remove(0);
                hboxDayHeaderContainer.getChildren().add(sundayDayHeaderPane);
                firstDayOfWeekDayTracker = false;
            }
        }
    }

    private AnchorPane[] listOfDayHeaders;

    private String dateViewed;

    private int dayViewed = 0;

    @FXML
    private void viewPreviousBlock(MouseEvent event) {
        if (defaultView) { //if viewing week
            return; //currently does nothing
        }
        else { //if viewing day
            dayViewed += (dayViewed == 0) ? 6 : -1;
            changeDayViewed(dayViewed);
        }
    }

    @FXML
    private void viewNextBlock(MouseEvent event) {
        if (defaultView) { //if viewing week
            return; //currently does nothing
        }
        else { //if viewing day
            dayViewed += (dayViewed == 6) ? -6 : 1;
            changeDayViewed(dayViewed);
        }
    }

    @FXML
    private void changeWeekViewed(String dateViewed) {
        return;
    }

    @FXML
    private void changeDayViewed(int dayViewed) {
        for (AnchorPane listOfDayHeader : listOfDayHeaders) {
            listOfDayHeader.setVisible(false);
        }
        listOfDayHeaders[dayViewed].setVisible(true);
    }

    @FXML
    private VBox root;

    private double offsetX = 0;
    private double offsetY = 0;

    @FXML
    private void recordWindowPosition(MouseEvent event) {
        offsetX = event.getSceneX();
        offsetY = event.getSceneY();
    }

    @FXML
    private void updateWindowPosition(MouseEvent event) {
        Launcher.getPrimaryStage().setX(event.getScreenX() - offsetX);
        Launcher.getPrimaryStage().setY(event.getScreenY() - offsetY);
    }

    @FXML
    private void minimizeWindow(MouseEvent event) {
        Launcher.getPrimaryStage().setIconified(true);
    }

    @FXML
    private void closeApplication(MouseEvent event) {
        Platform.exit();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listOfDayHeaders = new AnchorPane[] {
            sundayDayHeaderPane,
            mondayDayHeaderPane,
            tuesdayDayHeaderPane,
            wednesdayDayHeaderPane,
            thursdayDayHeaderPane,
            fridayDayHeaderPane,
            saturdayDayHeaderPane
        };

        viewingMenu.setVisible(false);
        viewingHitbox.setVisible(false);

        weekRectangle.setVisible(defaultView);
        dayRectangle.setVisible(!defaultView);

        sundayRectangle.setVisible(weekStart);
        mondayRectangle.setVisible(!weekStart);
    }
}
