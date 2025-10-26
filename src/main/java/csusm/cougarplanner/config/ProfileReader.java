package csusm.cougarplanner.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileReader
{
    private final Path profilePath;
    private final List<String> validationErrors;

    public ProfileReader(Path profilePath)
    {
        this.profilePath = profilePath;
        this.validationErrors = new ArrayList<>();
    }

    public ReadResult readProfile()
    {
        Map<String, String> properties = new HashMap<>();

        if (!Files.exists(profilePath))
        {
            validationErrors.add("Profile file does not exist, using defaults");
            return new ReadResult(new Profile(), validationErrors);
        }

        try {
            List<String> lines = Files.readAllLines(profilePath);

            for (int i = 0; i < lines.size(); i++)
            {
                String line = lines.get(i).trim();

                // Skips empty lines and comments
                if (line.isEmpty() || line.startsWith("#"))
                {
                    continue;
                }

                // Parses key=value pairs
                String[] parts = line.split("=", 2);
                if (parts.length == 2)
                {
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    properties.put(key, value);
                }
                else
                {
                    validationErrors.add("Invalid format on line " + (i + 1) + ": " + line);
                }
            }

            return new ReadResult(new Profile(properties), validationErrors);

        }
        catch (IOException e)
        {
            validationErrors.add("Failed to read profile file: " + e.getMessage());
            return new ReadResult(new Profile(), validationErrors);
        }
    }

    // Error handling
    public static class ReadResult
    {
        private final Profile profile;
        private final List<String> errors;

        public ReadResult(Profile profile, List<String> errors)
        {
            this.profile = profile;
            this.errors = new ArrayList<>(errors);
        }

        public Profile getProfile()
        {
            return profile;
        }
        public List<String> getErrors()
        {
            return errors;
        }
        public boolean hasErrors()
        {
            return !errors.isEmpty();
        }
    }
}