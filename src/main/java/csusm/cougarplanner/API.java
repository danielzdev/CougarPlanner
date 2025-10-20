package csusm.cougarplanner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class API {

    private final String AUTH_TOKEN;
    private final String baseURI = "https://csusm.instructure.com/api/v1/";

    private HttpClient client = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .followRedirects(HttpClient.Redirect.NORMAL)
        .connectTimeout(Duration.ofSeconds(3))
        .build();

    public API() {
        AUTH_TOKEN = getAuthToken();
    }

    /**
     * Retrieves the authentication token from the .profile configuration file.
     * The method expects the token to be stored in the format "Token = value" where
     * the actual token value follows the equals sign and a space.
     *
     * @return the authentication token as a String, or null if the file is not found
     *         or an error occurs during reading
     * @throws FileNotFoundException if the .profile file cannot be located
     */
    private static String getAuthToken() {
        try (Scanner sc = new Scanner(new File(".profile"))) {
            String line = sc.nextLine();
            int tokenStartIndex = line.indexOf("=") + 2;
            String token = line.substring(tokenStartIndex).trim();

            return token;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieves a list of active courses from the Canvas API.
     * This method makes an HTTP GET request to fetch courses where the user has
     * active enrollment. It validates internet connectivity and authentication
     * before making the request.
     *
     * @return an ArrayList of Course objects representing the user's active courses
     * @throws Exception if there's no internet connection, no auth token provided,
     *                   or if the HTTP request fails (including 401 Unauthorized
     *                   or other HTTP error codes)
     * @throws IllegalStateException if no internet connection is available,
     *                               if the auth token is null or blank,
     *                               if the response status is 401 (Unauthorized),
     *                               or if any other HTTP error occurs
     */
    public ArrayList<Course> getCourses() throws Exception {
        if (!hasInternetConnection()) {
            throw new IllegalStateException("Error: No internet connection");
        } else if (AUTH_TOKEN == null || AUTH_TOKEN.isBlank()) {
            throw new IllegalStateException("Error: No auth token provided");
        }

        String url = baseURI + "courses?enrollment_state=active&per_page=10";

        HttpRequest req = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .timeout(Duration.ofSeconds(20))
            .header("Authorization", "Bearer " + AUTH_TOKEN)
            .header("Accept", "application/json")
            .GET()
            .build();

        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());

        int code = res.statusCode();

        if (code == 401) {
            throw new IllegalStateException("Unauthorized (401): check your token.");
        } else if (code / 100 != 2) {
            throw new IllegalStateException("HTTP " + code + ": " + res.body());
        }

        Gson gson = new Gson();
        Type listType = new TypeToken<List<CourseDTO>>() {}.getType();
        List<CourseDTO> dtos = gson.fromJson(res.body(), listType);

        ArrayList<Course> courses = new ArrayList<>();
        if (dtos != null) {
            for (CourseDTO dto : dtos) {
                courses.add(toCourse(dto));
            }
        }

        return courses;
    }

    /**
     * Checks for an active internet connection.
     * This method is intended to verify network connectivity before making API calls.
     *
     * @return true if an internet connection is available, false otherwise
     */
    private boolean hasInternetConnection() {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("8.8.8.8", 53), 2000);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    static class CourseDTO {

        long id;
        String name;
    }

    /**
     * Converts a CourseDTO object to a Course object.
     * This method extracts the course code from the full course name and creates
     * a new Course instance with the extracted code and the DTO's ID.
     *
     * @param dto the CourseDTO object containing course data from the API
     * @return a new Course object with the extracted course code and string representation of the ID
     */
    private static Course toCourse(CourseDTO dto) {
        String courseCode = extractCourseCode(dto.name);
        return new Course(courseCode, String.valueOf(dto.id));
    }

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
    private static String extractCourseCode(String fullCourseName) {
        if (fullCourseName == null || fullCourseName.isEmpty()) {
            return fullCourseName;
        }

        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\b([A-Z]+\\s+\\d+)\\b");
        java.util.regex.Matcher matcher = pattern.matcher(fullCourseName);

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
