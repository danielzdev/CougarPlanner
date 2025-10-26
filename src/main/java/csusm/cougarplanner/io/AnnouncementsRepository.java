package csusm.cougarplanner.io;

import csusm.cougarplanner.Announcement;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import csusm.cougarplanner.util.DateTimeUtil;
import csusm.cougarplanner.util.WeekUtil;

public class AnnouncementsRepository
{
    private static final String[] HEADERS = {"announcement_id", "course_id", "title", "posted_at", "body"};
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final CsvReader csvReader;
    private final CsvWriter csvWriter;

    public AnnouncementsRepository()
    {
        this.csvReader = new CsvReader();
        this.csvWriter = new CsvWriter();
    }

    public List<Announcement> findAll() throws IOException
    {
        List<Map<String, String>> records = csvReader.readAll(CsvPaths.getAnnouncementsPath());
        return records.stream()
                .map(this::mapToAnnouncement)
                .collect(Collectors.toList());
    }

    public List<Announcement> findByWeek(LocalDate weekStart, LocalDate weekEnd) throws IOException
    {
        return findAll().stream()
                .filter(announcement -> isInWeek(announcement, weekStart, weekEnd))
                .collect(Collectors.toList());
    }

    public List<Announcement> findByDay(LocalDate day) throws IOException
    {
        return findAll().stream()
                .filter(announcement -> isOnDay(announcement, day))
                .collect(Collectors.toList());
    }

    public void upsert(Announcement announcement) throws IOException
    {
        List<Announcement> allAnnouncements = findAll();

        // Removes existing announcement with same ID
        allAnnouncements.removeIf(a -> a.getAnnouncementId().equals(announcement.getAnnouncementId()));

        // Adds updated announcement
        allAnnouncements.add(announcement);

        // Writes back to file
        List<Map<String, String>> records = allAnnouncements.stream()
                .map(this::announcementToMap)
                .collect(Collectors.toList());

        csvWriter.writeAll(CsvPaths.getAnnouncementsPath(), records, HEADERS);
    }

    public void upsertAll(List<Announcement> announcements) throws IOException
    {
        Map<String, Announcement> announcementMap = new HashMap<>();

        // Loads existing announcements
        for (Announcement existing : findAll())
        {
            announcementMap.put(existing.getAnnouncementId(), existing);
        }

        // Updates with new announcements
        for (Announcement announcement : announcements)
        {
            announcementMap.put(announcement.getAnnouncementId(), announcement);
        }

        // Writes back
        List<Map<String, String>> records = announcementMap.values().stream()
                .map(this::announcementToMap)
                .collect(Collectors.toList());

        csvWriter.writeAll(CsvPaths.getAnnouncementsPath(), records, HEADERS);
    }

    private boolean isInWeek(Announcement announcement, LocalDate weekStart, LocalDate weekEnd)
    {
        LocalDateTime postedAt = DateTimeUtil.parseDateTime(announcement.getPostedAt());
        if (postedAt == null) return false;
        LocalDate postedDate = postedAt.toLocalDate();
        return WeekUtil.isDateInWeek(postedDate, weekStart, weekEnd);
    }

    private boolean isOnDay(Announcement announcement, LocalDate day)
    {
        LocalDateTime postedAt = DateTimeUtil.parseDateTime(announcement.getPostedAt());
        return postedAt != null && postedAt.toLocalDate().equals(day);
    }

    private Announcement mapToAnnouncement(Map<String, String> record)
    {
        Announcement announcement = new Announcement();
        announcement.setAnnouncementId(record.get("announcement_id"));
        announcement.setCourseId(record.get("course_id"));
        announcement.setTitle(record.get("title"));
        announcement.setPostedAt(record.get("posted_at"));
        announcement.setBody(record.get("body"));
        return announcement;
    }

    private Map<String, String> announcementToMap(Announcement announcement)
    {
        Map<String, String> record = new HashMap<>();
        record.put("announcement_id", announcement.getAnnouncementId());
        record.put("course_id", announcement.getCourseId());
        record.put("title", announcement.getTitle());
        record.put("posted_at", announcement.getPostedAt());
        record.put("body", announcement.getBody() != null ? announcement.getBody() : "");
        return record;
    }
}
