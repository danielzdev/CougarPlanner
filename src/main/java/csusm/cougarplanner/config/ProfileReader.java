package csusm.cougarplanner.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Reads and parses the application config from the .profile file.
 */
public class ProfileReader
{
    private final Path profilePath; // Path to the .profile config file
    private final List<String> validationErrors; // Validation & parsing errors from reading

    /**
     * Constructs a ProfileReader for the specified .profile file.
     *
     * @param profilePath the filesystem path to the .profile configuration file
     */
    public ProfileReader(Path profilePath)
    {
        this.profilePath = profilePath;
        this.validationErrors = new ArrayList<>();
    }

    /**
     * - Reads the .profile file line by line
     * - Parses key=value pairs with whitespace trimming
     * - Skips empty lines and comment lines (starting with #)
     * - Collects all parsing errors for UI display
     * - Falls back to default values for missing keys
     * - Returns default Profile if file doesn't exist or can't be read
     */
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

    /**
     * Represents profile reading operation with parsed Profile and any errors.
     * Enables UI to display warnings without preventing application startup.
     *
     */
    public static class ReadResult
    {
        private final Profile profile; // Successfully parsed Profile object
        private final List<String> errors; // List of parsing & validation errors, empty if none

        /**
         * Constructs a new ReadResult with the given profile and errors.
         *
         * @param profile the parsed Profile object (uses defaults if parsing failed)
         * @param errors list of errors encountered during parsing
         */
        public ReadResult(Profile profile, List<String> errors)
        {
            this.profile = profile;
            this.errors = new ArrayList<>(errors);
        }

        /**
         * Gets the parsed Profile object.
         * This is always non-null - if parsing failed, returns a Profile with default values.
         *
         * @return the parsed Profile configuration
         */
        public Profile getProfile()
        {
            return profile;
        }

        /**
         * Gets the list of parsing and validation errors.
         *
         * @return a copy of the error list, empty if none
         */
        public List<String> getErrors()
        {
            return errors;
        }

        /**
         * Checks whether any errors were encountered during profile reading.
         *
         * @return true if one or more errors occurred, false if none
         */
        public boolean hasErrors()
        {
            return !errors.isEmpty();
        }
    }
}