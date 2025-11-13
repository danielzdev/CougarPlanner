package csusm.cougarplanner.services;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * This interface defines what all data providers must do. Data providers connect the data layer and the user interface,
 * preparing display-ready data with course names and handling empty results.
 *
 * @param <T> The type of display object this provider returns (e.g., AssignmentDisplay, AnnouncementDisplay)
 */
public interface ViewDataProvider<T>
{

    /**
     * Fetches data for a chosen time period, adds related course information, and prepares it for display.
     *
     * @param periodStart The start date of the period
     * @param periodEnd The end date of the period
     * @return List of enriched display objects ready for UI presentation
     * @throws IOException If data cannot be loaded from underlying repositories
     */
    List<T> getDataForPeriod(LocalDate periodStart, LocalDate periodEnd) throws IOException;

    /**
     * Provides a user-friendly message to display when no data is available.
     *
     * @return Friendly empty state message
     */
    String getEmptyMessage();
}
