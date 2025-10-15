package csusm.cougarplanner;

public class ProfileConfig
{
    private String token;
    private boolean orientationCompleted;
    private String order;
    private String weekStartingDay;

    public ProfileConfig()
    {
        this.token = "";
        this.orientationCompleted = false;
        this.order = "COURSE_THEN_DATE";
        this.weekStartingDay = "MONDAY";
    }

    public ProfileConfig(String token, boolean orientationCompleted, String order, String weekStartingDay)
    {
        this.token = token;
        this.orientationCompleted = orientationCompleted;
        this.order = order;
        this.weekStartingDay = weekStartingDay;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public boolean isOrientationCompleted()
    {
        return orientationCompleted;
    }

    public void setOrientationCompleted(boolean orientationCompleted)
    {
        this.orientationCompleted = orientationCompleted;
    }

    public String getOrder()
    {
        return order;
    }

    public void setOrder(String order)
    {
        this.order = order;
    }

    public String getWeekStartingDay()
    {
        return weekStartingDay;
    }

    public void setWeekStartingDay(String weekStartingDay)
        {
        this.weekStartingDay = weekStartingDay;
        }
}
