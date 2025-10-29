package csusm.cougarplanner.util;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class CourseCodeUtil {

    private static final Pattern COURSE_CODE_PATTERN = Pattern.compile("\\b([A-Z]+\\s+\\d+)\\b");

    /**
     * Extracts a course code from a full course name string.
     * This method attempts to parse course codes using multiple strategies:
     * 1. First tries to match a pattern of uppercase letters followed by numbers (e.g., "CS 101")
     * 2. If no match is found, splits the string on hyphens or colons and takes the first part
     * 3. Returns the original string if no extraction is possible
     *
     * @param fullCourseName the complete course name string to extract the code from
     * @return the extracted course code, or the original string if extraction fails or input is null/empty
     */
    public static String extract(String fullCourseName) {
        if (fullCourseName == null || fullCourseName.isEmpty()) {
            return fullCourseName;
        }

        Matcher matcher = COURSE_CODE_PATTERN.matcher(fullCourseName);

        if (matcher.find()) {
            return matcher.group(1);
        }

        String[] parts = fullCourseName.split("\\s*[-:]\\s*");
        if (parts.length > 0) {
            return parts[0].trim();
        }

        return fullCourseName;
    }
}
