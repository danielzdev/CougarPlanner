package csusm.cougarplanner;

public class Assignment
{
    private String assignment_id;
    private String course_id;
    private String assignment_name;
    private String due_date;
    private String due_time;
    private Integer difficulty;

    public Assignment() {}

    public Assignment(String assignment_id, String course_id, String assignment_name,
                      String due_date, String due_time, Integer difficulty)
    {
        this.assignment_id = assignment_id;
        this.course_id = course_id;
        this.assignment_name = assignment_name;
        this.due_date = due_date;
        this.due_time = due_time;
        this.difficulty = difficulty;
    }

    public String getAssignmentId()
    {
        return assignment_id;
    }

    public void setAssignmentId(String assignment_id)
    {
        this.assignment_id = assignment_id;
    }

    public String getCourseId()
    {
        return course_id;
    }

    public void setCourseId(String course_id)
    {
        this.course_id = course_id;
    }

    public String getAssignmentName()
    {
        return assignment_name;
    }

    public void setAssignmentName(String assignment_name)
    {
        this.assignment_name = assignment_name;
    }

    public String getDueDate()
    {
        return due_date;
    }

    public void setDueDate(String due_date)
    {
        this.due_date = due_date;
    }

    public String getDueTime()
    {
        return due_time;
    }

    public void setDueTime(String due_time)
    {
        this.due_time = due_time;
    }

    public Integer getDifficulty()
    {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty)
    {
        this.difficulty = difficulty;
    }

    @Override
    public String toString()
    {
        return "Assignment{" +
                "assignment_id='" + assignment_id + '\'' +
                ", course_id='" + course_id + '\'' +
                ", assignment_name='" + assignment_name + '\'' +
                ", due_date='" + due_date + '\'' +
                ", due_time='" + due_time + '\'' +
                ", difficulty=" + difficulty +
                '}';
    }
}