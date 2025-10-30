package csusm.cougarplanner;

import csusm.cougarplanner.config.Profile;
import csusm.cougarplanner.config.ProfileReader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.nio.file.Path;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    ProfileReader reader = new ProfileReader(Path.of("data/profile.properties"));
    Profile profile = reader.readProfile().getProfile();

    @FXML
    private AnchorPane viewingMenu;

    boolean viewingMenuIsOpen = false;

    @FXML
    private void toggleOrderMenu(MouseEvent event) {
        viewingMenu.setVisible(!viewingMenuIsOpen);
        viewingMenuIsOpen = !viewingMenuIsOpen;
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

                defaultView = userClickedViewByWeek;
                profile.setDefaultView(defaultView ? "week" : "day");
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

            if (weekStart != userClickedSunday) {
                sundayRectangle.setVisible(userClickedSunday);
                mondayRectangle.setVisible(!userClickedSunday);

                weekStart = userClickedSunday;
                profile.setWeekStart(weekStart ? "sunday" : "monday");

                organizePlannerByWeekStart(event);
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

    private void organizePlannerByWeekStart(MouseEvent event) {
        boolean newWeekStart = profile.getWeekStart().equals("sunday");

        if (newWeekStart) { //change the week beginning to sunday
            hboxWeekHeaderContainer.getChildren().removeAll(mondayHeaderPane, tuesdayHeaderPane, wednesdayHeaderPane, thursdayHeaderPane, fridayHeaderPane, saturdayHeaderPane);
            hboxWeekHeaderContainer.getChildren().addAll(mondayHeaderPane, tuesdayHeaderPane, wednesdayHeaderPane, thursdayHeaderPane, fridayHeaderPane, saturdayHeaderPane);
            hboxWeekContentsContainer.getChildren().removeAll(mondayContentsPane, tuesdayContentsPane, wednesdayContentsPane, thursdayContentsPane, fridayContentsPane, saturdayContentsPane);
            hboxWeekContentsContainer.getChildren().addAll(mondayContentsPane, tuesdayContentsPane, wednesdayContentsPane, thursdayContentsPane, fridayContentsPane, saturdayContentsPane);
        }
        else { //change the week beginning to monday
            hboxWeekHeaderContainer.getChildren().remove(0);
            hboxWeekHeaderContainer.getChildren().add(sundayHeaderPane);
            hboxWeekContentsContainer.getChildren().remove(0);
            hboxWeekContentsContainer.getChildren().add(sundayContentsPane);
        }
    }

    @FXML
    private HBox hboxDayHeaderContainer;

    @FXML
    private AnchorPane sundayDayHeaderPane, mondayDayHeaderPane, tuesdayDayHeaderPane, wednesdayDayHeaderPane, thursdayDayHeaderPane, fridayDayHeaderPane, saturdayDayHeaderPane;

    private String dateViewed;

    private int dayViewed = 0;

    @FXML
    private void viewPreviousBlock(MouseEvent event) {
        if (defaultView) { //if viewing week
            return;
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
            dayViewed += (dayViewed == 6) ? 0 : 1;
            changeDayViewed(dayViewed);
        }
    }

    @FXML
    private void changeWeekViewed(String dateViewed) {
        return;
    }

    @FXML
    private void changeDayViewed(int dayViewed) {
        return;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        weekRectangle.setVisible(defaultView);
        dayRectangle.setVisible(!defaultView);

        sundayRectangle.setVisible(weekStart);
        mondayRectangle.setVisible(!weekStart);
    }
}
