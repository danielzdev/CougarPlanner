package csusm.cougarplanner.config;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Profile
{

    private final Map<String, String> properties;


    private static final Map<String, String> DEFAULTS = createDefaults();

    private static Map<String, String> createDefaults()
    {
        Map<String, String> defaults = new HashMap<>();
        defaults.put("authToken", "");
        defaults.put("orientationCompleted", "false");
        defaults.put("storeData", "true");
        defaults.put("storeToken", "true");
        defaults.put("weekStart", "monday");
        defaults.put("defaultView", "week");
        defaults.put("openTo", "current_week");
        defaults.put("defaultSort.mode", "date_time");
        defaults.put("defaultSort.difficultyOrder", "ascending");
        defaults.put("showRefreshCountdown", "false");
        defaults.put("lastViewedWeekStart", "");
        defaults.put("lastViewedDay", "");
        return defaults;
    }

    public Profile()
    {
        this.properties = new HashMap<>(DEFAULTS);
    }

    public Profile(Map<String, String> properties)
    {
        this.properties = new HashMap<>(DEFAULTS);
        this.properties.putAll(properties);
    }

    public String getAuthToken() { return properties.get("authToken"); }
    public String getOrientationCompleted() { return properties.get("orientationCompleted"); }
    public String getStoreData() { return properties.get("storeData"); }
    public String getStoreToken() { return properties.get("storeToken"); }
    public String getWeekStart() { return properties.get("weekStart"); }
    public String getDefaultView() { return properties.get("defaultView"); }
    public String getOpenTo() { return properties.get("openTo"); }
    public String getDefaultSortMode() { return properties.get("defaultSort.mode"); }
    public String getDefaultSortDifficultyOrder() { return properties.get("defaultSort.difficultyOrder"); }
    public String getShowRefreshCountdown() { return properties.get("showRefreshCountdown"); }
    public String getLastViewedWeekStart() { return properties.get("lastViewedWeekStart"); }
    public String getLastViewedDay() { return properties.get("lastViewedDay"); }

    public boolean isOrientationCompleted()
    {
        return "true".equals(getOrientationCompleted());
    }

    public boolean shouldStoreData()
    {
        return "true".equals(getStoreData());
    }

    public boolean shouldStoreToken()
    {
        return "true".equals(getStoreToken());
    }

    public boolean shouldShowRefreshCountdown()
    {
        return "true".equals(getShowRefreshCountdown());
    }

    public LocalDate getLastViewedWeekStartAsDate()
    {
        String dateStr = getLastViewedWeekStart();
        return dateStr != null && !dateStr.isEmpty() ? LocalDate.parse(dateStr) : null;
    }

    public LocalDate getLastViewedDayAsDate()
    {
        String dateStr = getLastViewedDay();
        return dateStr != null && !dateStr.isEmpty() ? LocalDate.parse(dateStr) : null;
    }

    public void setAuthToken(String token)
    {
        properties.put("authToken", token);
    }
    public void setOrientationCompleted(boolean completed)
    {
        properties.put("orientationCompleted", String.valueOf(completed));
    }
    public void setStoreData(boolean storeData)
    {
        properties.put("storeData", String.valueOf(storeData));
    }
    public void setStoreToken(boolean storeToken)
    {
        properties.put("storeToken", String.valueOf(storeToken));
    }
    public void setWeekStart(String weekStart)
    {
        properties.put("weekStart", weekStart);
    }
    public void setDefaultView(String defaultView)
    {
        properties.put("defaultView", defaultView);
    }
    public void setOpenTo(String openTo)
    {
        properties.put("openTo", openTo);
    }
    public void setDefaultSortMode(String sortMode)
    {
        properties.put("defaultSort.mode", sortMode);
    }
    public void setDefaultSortDifficultyOrder(String difficultyOrder)
    {
        properties.put("defaultSort.difficultyOrder", difficultyOrder);
    }
    public void setShowRefreshCountdown(boolean showCountdown)
    {
        properties.put("showRefreshCountdown", String.valueOf(showCountdown));
    }
    public void setLastViewedWeekStart(String weekStart)
    {
        properties.put("lastViewedWeekStart", weekStart);
    }
    public void setLastViewedDay(String day)
    {
        properties.put("lastViewedDay", day);
    }

    public Map<String, String> getAllProperties()
    {
        return new HashMap<>(properties);
    }
}