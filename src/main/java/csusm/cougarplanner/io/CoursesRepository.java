package csusm.cougarplanner.io;

import csusm.cougarplanner.Course;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Repository class for managing Course data persistence in courses.csv.
 */
public class CoursesRepository
{
    // CSV column headers matching the courses.csv file specification
    private static final String[] HEADERS = {"course_id", "course_name"};
    private final CsvReader csvReader;
    private final CsvWriter csvWriter;

    /**
     * Constructs a new CoursesRepository with default CSV reader/writer.
     */
    public CoursesRepository()
    {
        this.csvReader = new CsvReader();
        this.csvWriter = new CsvWriter();
    }

    /**
     * Retrieves all courses from the courses.csv file.
     * Courses are used as reference data for assignments and announcements.
     *
     * @return List of all Course objects in the database
     * @throws IOException if the CSV file cannot be read
     */
    public List<Course> findAll() throws IOException
    {
        List<Map<String, String>> records = csvReader.readAll(CsvPaths.getCoursesPath());
        return records.stream()
                .map(this::mapToCourse)
                .collect(Collectors.toList());
    }

    /**
     * Updates an existing course or inserts a new one.
     *
     * @param course the Course object to update or insert
     * @throws IOException if the CSV file cannot be written
     */
    public void upsert(Course course) throws IOException
    {
        List<Course> allCourses = findAll();

        // Removes existing course with same ID
        allCourses.removeIf(c -> c.getCourseId().equals(course.getCourseId()));

        // Adds updated course
        allCourses.add(course);

        // Writes back to file
        List<Map<String, String>> records = allCourses.stream()
                .map(this::courseToMap)
                .collect(Collectors.toList());

        csvWriter.writeAll(CsvPaths.getCoursesPath(), records, HEADERS);
    }

    /**
     * Upsert operation on multiple courses.
     *
     * @param courses List of courses to upsert
     * @throws IOException if the CSV file cannot be written
     */
    public void upsertAll(List<Course> courses) throws IOException
    {
        Map<String, Course> courseMap = new HashMap<>();

        // Loads existing courses
        for (Course existing : findAll())
        {
            courseMap.put(existing.getCourseId(), existing);
        }

        // Updates with new courses
        for (Course course : courses)
        {
            courseMap.put(course.getCourseId(), course);
        }

        // Writes back
        List<Map<String, String>> records = courseMap.values().stream()
                .map(this::courseToMap)
                .collect(Collectors.toList());

        csvWriter.writeAll(CsvPaths.getCoursesPath(), records, HEADERS);
    }

    /**
     * Converts a CSV record Map to a Course object.
     * Maps snake_case CSV headers to Course object properties.
     *
     * @param record Map representing a CSV row with snake_case keys
     * @return Course object populated from the CSV data
     */
    private Course mapToCourse(Map<String, String> record)
    {
        Course course = new Course();
        course.setCourseId(record.get("course_id"));
        course.setCourseName(record.get("course_name"));
        return course;
    }

    /**
     * Converts a Course object to a CSV record Map.
     * Ensures data is formatted correctly for CSV serialization.
     *
     * @param course the Course object to convert
     * @return Map representing a CSV row with snake_case keys
     */
    private Map<String, String> courseToMap(Course course)
    {
        Map<String, String> record = new HashMap<>();
        record.put("course_id", course.getCourseId());
        record.put("course_name", course.getCourseName());
        return record;
    }
}
