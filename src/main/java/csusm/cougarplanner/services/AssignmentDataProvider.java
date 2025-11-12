package csusm.cougarplanner.services;

import csusm.cougarplanner.models.Assignment;
import csusm.cougarplanner.models.AssignmentDisplay;
import csusm.cougarplanner.io.AssignmentsRepository;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides assignment data for the user interface.
 * Fetches, filters, and adds course names to assignment details.
 * Accesses data through the AssignmentsRepository.
 */
public class AssignmentDataProvider extends AbstractItemDataProvider<Assignment, AssignmentDisplay>
{

    /**
     * Repository for accessing assignment data from assignments.csv.
     * Provides methods for finding assignments by week and day ranges.
     */
    private final AssignmentsRepository assignmentsRepository;

    /**
     * Constructs a new AssignmentDataProvider with required dependencies.
     * Initializes the assignments repository for data access.
     */
    public AssignmentDataProvider()
    {
        super();
        this.assignmentsRepository = new AssignmentsRepository();
    }

    /**
     * Retrieves assignments for a specific time period and enriches them with course names.
     * This is the primary method used by week view in the main application.
     *
     * @param periodStart The start date of the week (inclusive)
     * @param periodEnd The end date of the week (inclusive)
     * @return List of assignment display objects ready for UI presentation
     * @throws IOException If assignment or course data cannot be accessed
     */
    @Override
    public List<AssignmentDisplay> getDataForPeriod(LocalDate periodStart, LocalDate periodEnd) throws IOException
    {
        // Uses existing repository method for week-based queries
        List<Assignment> assignments = assignmentsRepository.findByWeek(periodStart, periodEnd);

        // Enriches assignments with course names and convert to display objects
        return enrichItemsWithCourseNames(assignments);
    }

    /**
     * Retrieves assignments for a specific day and enriches them with course names.
     * This method supports the day view functionality in the main application.
     *
     * @param day The specific day to retrieve assignments for
     * @return List of assignment display objects for the specified day
     * @throws IOException If assignment or course data cannot be accessed
     */
    public List<AssignmentDisplay> getDataForDay(LocalDate day) throws IOException
    {
        List<Assignment> assignments = assignmentsRepository.findByDay(day);
        return enrichItemsWithCourseNames(assignments);
    }

    /**
     * Enriches assignment objects with course names by looking up course information.
     * Transforms base Assignment objects into AssignmentDisplay objects with full course context.
     *
     * @param assignments List of base assignment objects from repository
     * @return List of enriched assignment display objects
     * @throws IOException If course data cannot be accessed for lookup
     */
    @Override
    protected List<AssignmentDisplay> enrichItemsWithCourseNames(List<Assignment> assignments) throws IOException
    {
        List<AssignmentDisplay> enrichedAssignments = new ArrayList<>();

        for (Assignment assignment : assignments)
        {
            // Looks up course name using the course_id from the assignment
            String courseName = getCourseName(assignment.getCourseId());

            // Creates display object with enriched course information
            AssignmentDisplay displayItem = new AssignmentDisplay(assignment, courseName);
            enrichedAssignments.add(displayItem);
        }

        return enrichedAssignments;
    }

    /**
     * Provides a user-friendly message for empty assignment states.
     *
     * @return Empty state message for assignments
     */
    @Override
    public String getEmptyMessage()
    {
        return "No assignments due for this class.";
    }
}