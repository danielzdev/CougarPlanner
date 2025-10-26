package csusm.cougarplanner.io;

import java.nio.file.Path;
import java.nio.file.Paths;

public class CsvPaths
{
    private static final String DATA_DIR = "data";

    // CSV file paths
    public static Path getCoursesPath()
    {
        return Paths.get(DATA_DIR, "courses.csv");
    }

    public static Path getAssignmentsPath()
    {
        return Paths.get(DATA_DIR, "assignments.csv");
    }

    public static Path getAnnouncementsPath()
    {
        return Paths.get(DATA_DIR, "announcements.csv");
    }

    public static Path getSnapshotsPath()
    {
        return Paths.get(DATA_DIR, "snapshots.json");
    }

    // Ensures data directory exists
    public static void ensureDataDirectory()
    {
        Path dataDir = Paths.get(DATA_DIR);
        if (!dataDir.toFile().exists()) {
            dataDir.toFile().mkdirs();
        }
    }
}