package csusm.cougarplanner;

import csusm.cougarplanner.ProfileConfig;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ProfileStorage
{

    private static final String APP_DIR_NAME = ".cougar-planner";
    private static final String PROFILE_FILE_NAME = ".profile";

    private static Path getProfilePath()
    {
        // Gets user's home directory
        String userHome = System.getProperty("user.home");
        Path appDirPath = Paths.get(userHome, APP_DIR_NAME);
        return appDirPath.resolve(PROFILE_FILE_NAME);
    }

    // Checks that the ~/.cougar-planner/ directory exists
    private static void ensureAppDirectoryExists() throws IOException
    {
        Path appDirPath = getProfilePath().getParent();
        if (!Files.exists(appDirPath))
        {
            Files.createDirectories(appDirPath);
            System.out.println("Created application directory: " + appDirPath);
        }
    }

    // Saves the current config model into user's path using CSV format
    public static void saveProfile(ProfileConfig config)
    {
        try
        {
            ensureAppDirectoryExists();
            Path profilePath = getProfilePath();

            // Converts the ProfileConfig object fields into a comma-separated string format with column order
            String csvLine = String.join(",",
                    config.getToken(),
                    String.valueOf(config.isOrientationCompleted()),
                    config.getOrder(),
                    config.getWeekStartingDay()
            );

            // Writes to file
            try (BufferedWriter writer = Files.newBufferedWriter(profilePath))
            {
                writer.write(csvLine);
            }

            System.out.println("Profile saved successfully to: " + profilePath);

        }
        catch (IOException e)
        {
            System.err.println("Error saving profile: " + e.getMessage());
            System.err.println("Error saving profile: " + e.getMessage());
            e.printStackTrace(); // for debugging
        }
    }

    // Reads .profile file
    public static ProfileConfig loadProfile()
    {
        Path profilePath = getProfilePath();

        // Checks .profile file exists
        // If file does not exist (first run), returns a default ProfileConfig object where orientationCompleted = false
        if (!Files.exists(profilePath))
        {
            System.out.println("No profile file found. Returning default config for first run.");
            return new ProfileConfig();
        }

        try (BufferedReader reader = Files.newBufferedReader(profilePath))
        {
            String csvLine = reader.readLine();

            // If file exists, reads CSV lines & converts CSV string back to object
            if (csvLine != null && !csvLine.trim().isEmpty())
            {
                String[] values = csvLine.split(",", -1); // -1 handles empty fields

                // Checks for correct number of fields
                if (values.length >= 4)
                {
                    String token = values[0];
                    boolean orientationCompleted = Boolean.parseBoolean(values[1]);
                    String order = values[2];
                    String weekStartingDay = values[3];

                    System.out.println("Profile loaded successfully");
                    return new ProfileConfig(token, orientationCompleted, order, weekStartingDay);
                }
                // Returns default config if file is corrupted
                else
                {
                    System.err.println("Profile file corrupted: incorrect number of fields");
                    return new ProfileConfig();
                }
            }

        }
        catch (IOException e)
        {
            System.err.println("Error loading profile: " + e.getMessage());
        }
        return new ProfileConfig();
    }

    // Debugging
    public static void printStorageInfo()
    {
        Path profilePath = getProfilePath();
        System.out.println("Profile storage path: " + profilePath);
        System.out.println("Profile file exists: " + Files.exists(profilePath));
        System.out.println("App directory exists: " + Files.exists(profilePath.getParent()));
    }

}
