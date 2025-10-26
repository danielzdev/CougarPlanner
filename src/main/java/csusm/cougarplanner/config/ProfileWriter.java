package csusm.cougarplanner.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProfileWriter
{
    private final Path profilePath;

    public ProfileWriter(Path profilePath)
    {
        this.profilePath = profilePath;
    }

    public void writeProfile(Profile profile) throws IOException
    {
        // Creates directory if it doesn't exist
        Path parentDir = profilePath.getParent();
        if (parentDir != null && !Files.exists(parentDir))
        {
            Files.createDirectories(parentDir);
        }

        // Creates temporary file
        Path tempFile = profilePath.resolveSibling(profilePath.getFileName() + ".tmp");

        // Writes to temporary file with fixed order
        List<String> lines = buildProfileLines(profile);
        Files.write(tempFile, lines);

        // Atomic move
        Files.move(tempFile, profilePath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
    }

    public void updateProfileFields(Map<String, String> updates) throws IOException
    {
        // Reads existing profile
        ProfileReader reader = new ProfileReader(profilePath);
        ProfileReader.ReadResult result = reader.readProfile();
        Profile profile = result.getProfile();

        // Applies updates
        Map<String, String> allProperties = profile.getAllProperties();
        allProperties.putAll(updates);

        // Writes updated profile
        writeProfile(new Profile(allProperties));
    }

    private List<String> buildProfileLines(Profile profile)
    {
        List<String> lines = new ArrayList<>();
        Map<String, String> properties = profile.getAllProperties();

        // Fixed order with authToken always first
        String[] keyOrder =
                {
                    "authToken",
                    "orientationCompleted",
                    "storeData",
                    "storeToken",
                    "weekStart",
                    "defaultView",
                    "openTo",
                    "defaultSort.mode",
                    "defaultSort.difficultyOrder",
                    "showRefreshCountdown",
                    "lastViewedWeekStart",
                    "lastViewedDay"
                };

        for (String key : keyOrder)
        {
            String value = properties.get(key);
            if (value != null)
            {
                lines.add(key + "=" + value);
            }
        }

        return lines;
    }
}