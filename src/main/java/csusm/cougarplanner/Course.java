package csusm.cougarplanner;

public class Course
{
    private String courseName;
    private String id;

    public Course(String courseName, String id)
    {
        this.courseName = courseName;
        this.id = id;
    }

    public String getCourseName()
    {
        return courseName;
    }

    public void setCourseName(String courseName)
    {
        this.courseName = courseName;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }
}
