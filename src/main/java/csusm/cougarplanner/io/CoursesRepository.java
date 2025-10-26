package csusm.cougarplanner.io;

import csusm.cougarplanner.Course;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class CoursesRepository
{
    private static final String[] HEADERS = {"course_id", "course_name"};
    private final CsvReader csvReader;
    private final CsvWriter csvWriter;

    public CoursesRepository()
    {
        this.csvReader = new CsvReader();
        this.csvWriter = new CsvWriter();
    }

    public List<Course> findAll() throws IOException
    {
        List<Map<String, String>> records = csvReader.readAll(CsvPaths.getCoursesPath());
        return records.stream()
                .map(this::mapToCourse)
                .collect(Collectors.toList());
    }

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

    private Course mapToCourse(Map<String, String> record)
    {
        Course course = new Course();
        course.setCourseId(record.get("course_id"));
        course.setCourseName(record.get("course_name"));
        return course;
    }

    private Map<String, String> courseToMap(Course course)
    {
        Map<String, String> record = new HashMap<>();
        record.put("course_id", course.getCourseId());
        record.put("course_name", course.getCourseName());
        return record;
    }
}
