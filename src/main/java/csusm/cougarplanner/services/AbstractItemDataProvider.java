package csusm.cougarplanner.services;

import csusm.cougarplanner.models.Course;
import csusm.cougarplanner.io.CoursesRepository;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Abstract base class that provides shared features for all item data providers.
 * It manages access to course data, offers fast course name lookups, and defines a common process for enriching data.
 *
 * @param <T> The type of domain object this provider handles (e.g., Assignment, Announcement)
 * @param <R> The type of display object this provider returns (e.g., AssignmentDisplay, AnnouncementDisplay)
 */
public abstract class AbstractItemDataProvider<T, R> implements ViewDataProvider<R>
{
    /**
     * Repository that accesses course data from courses.csv.
     * Maps course_id to course_name for data enrichment.
     */
    protected final CoursesRepository coursesRepository;

    /**
     * Stores course ID-to-name mappings to avoid repeated database lookups.
     * Improves performance when handling many items from the same courses.
     */
    private Map<String, String> courseIdToNameMap;

    /**
     * Marks whether the course data has been loaded into the cache.
     * Prevents reloading so courses are fetched only once per provider instance.
     */
    private boolean coursesLoaded = false;

    /**
     * Constructs a new AbstractItemDataProvider and initializes dependencies.
     */
    public AbstractItemDataProvider()

    {
        this.coursesRepository = new CoursesRepository();
        this.courseIdToNameMap = new HashMap<>();
    }

    /**
     * Gets all courses from storage and keeps them in memory for quick lookups.
     * Only loads them the first time they’re needed.
     *
     * @throws IOException If the courses.csv file cannot be read
     */
    protected void loadCoursesMap() throws IOException
    {
        if (!coursesLoaded)
        {
            List<Course> courses = coursesRepository.findAll();
            for (Course course : courses)
            {
                courseIdToNameMap.put(course.getCourseId(), course.getCourseName());
            }
            coursesLoaded = true;
        }
    }

    /**
     * Retrieves the course name for a given course ID.
     * Automatically loads course data if not already cached.
     * Returns "Unknown Course" if the course ID is not found.
     *
     * @param courseId The course ID to look up
     * @return The course name, or "Unknown Course" if not found
     * @throws IOException If course data cannot be loaded
     */
    protected String getCourseName(String courseId) throws IOException
    {
        loadCoursesMap();
        return courseIdToNameMap.getOrDefault(courseId, "Unknown Course");
    }

    /**
     * Template method that adds course names to domain objects.
     * Subclasses must define how to convert domain objects into display objects with this course information.
     *
     * @param items List of domain objects to enrich
     * @return List of display objects with course names
     * @throws IOException If course data cannot be accessed
     */
    protected abstract List<R> enrichItemsWithCourseNames(List<T> items) throws IOException;
}
