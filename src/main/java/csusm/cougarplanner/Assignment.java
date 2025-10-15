package csusm.cougarplanner;

import java.time.LocalDate;
import java.time.LocalTime;

public class Assignment
{
    private LocalDate date;
    private LocalTime time;
    private String course;
    private String assignmentName;
    private int difficultyLevel;


    public Assignment(LocalDate date, LocalTime time, String course, String assignmentName, int difficultyLevel)
    {
        this.date = date;
        this.time = time;
        this.course = course;
        this.assignmentName = assignmentName;
        this.difficultyLevel = difficultyLevel;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public void setDate(LocalDate date)
    {
        this.date = date;
    }

    public LocalTime getTime()
    {
        return time;
    }

    public void setTime(LocalTime time)
    {
        this.time = time;
    }

    public String getCourse()
    {
        return course;
    }

    public void setCourse(String course)
    {
        this.course = course;
    }

    public String getAssignmentName()
    {
        return assignmentName;
    }

    public void setAssignmentName(String assignmentName)
    {
        this.assignmentName = assignmentName;
    }

    public int getDifficultyLevel()
    {
        return difficultyLevel;
    }

    public void setDifficultyLevel(int difficultyLevel)
        {
        this.difficultyLevel = difficultyLevel;
        }
}
