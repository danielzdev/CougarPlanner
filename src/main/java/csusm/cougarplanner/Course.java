package csusm.cougarplanner;

/**
 * Represents a course with basic identification information.
 * Maps directly to the courses.csv file structure.
 * Serves as reference data for assignments and announcements.
 */
public class Course
{
    private String course_id;
    private String course_name;

    /**
     * Default constructor for CSV reading and object creation.
     * Required in repository layer.
     */
    public Course() {}

    /**
     * Constructs a complete Course with identifier and name.
     *
     * @param course_id unique identifier for the course
     * @param course_name the display name of the course
     */
    public Course(String course_id, String course_name)
    {
        this.course_id = course_id;
        this.course_name = course_name;
    }

    /**
     * Gets the unique course identifier.
     * Used as foreign key in assignments and announcements.
     *
     * @return the course_id as a string
     */
    public String getCourseId()
    {
        return course_id;
    }

    /**
     * Sets the unique course identifier.
     *
     * @param course_id the course_id to set
     */
    public void setCourseId(String course_id)
    {
        this.course_id = course_id;
    }

    /**
     * Gets the course display name.
     * Used for UI presentation and joining with assignment/announcement data.
     *
     * @return the course_name as a string
     */
    public String getCourseName()
    {
        return course_name;
    }

    /**
     * Sets the course display name.
     *
     * @param course_name the course_name to set
     */
    public void setCourseName(String course_name)
    {
        this.course_name = course_name;
    }

    /**
     * Returns a string representation of the course for debugging.
     *
     * @return string containing course identifier and name
     */
    @Override
    public String toString()
    {
        return "Course{" +
                "course_id='" + course_id + '\'' +
                ", course_name='" + course_name + '\'' +
                '}';
    }
}
