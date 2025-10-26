package csusm.cougarplanner;

public class Course
{
    private String course_id;
    private String course_name;

    public Course() {}

    public Course(String course_id, String course_name)
    {
        this.course_id = course_id;
        this.course_name = course_name;
    }

    public String getCourseId()
    {
        return course_id;
    }

    public void setCourseId(String course_id)
    {
        this.course_id = course_id;
    }

    public String getCourseName()
    {
        return course_name;
    }

    public void setCourseName(String course_name)
    {
        this.course_name = course_name;
    }

    @Override
    public String toString()
    {
        return "Course{" +
                "course_id='" + course_id + '\'' +
                ", course_name='" + course_name + '\'' +
                '}';
    }
}
