package csusm.cougarplanner;

public class Announcement
{
    private String announcement_id;
    private String course_id;
    private String title;
    private String posted_at;
    private String body;

    public Announcement() {}

    public Announcement(String announcement_id, String course_id, String title,
                        String posted_at, String body)
    {
        this.announcement_id = announcement_id;
        this.course_id = course_id;
        this.title = title;
        this.posted_at = posted_at;
        this.body = body;
    }

    public String getAnnouncementId()
    {
        return announcement_id;
    }

    public void setAnnouncementId(String announcement_id)
    {
        this.announcement_id = announcement_id;
    }

    public String getCourseId()
    {
        return course_id;
    }

    public void setCourseId(String course_id)
    {
        this.course_id = course_id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getPostedAt()
    {
        return posted_at;
    }

    public void setPostedAt(String posted_at)
    {
        this.posted_at = posted_at;
    }

    public String getBody()
    {
        return body;
    }

    public void setBody(String body)
    {
        this.body = body;
    }

    @Override
    public String toString()
    {
        return "Announcement{" +
                "announcement_id='" + announcement_id + '\'' +
                ", course_id='" + course_id + '\'' +
                ", title='" + title + '\'' +
                ", posted_at='" + posted_at + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}