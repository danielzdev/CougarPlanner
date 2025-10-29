package csusm.cougarplanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
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
     * Performs GET request to fetch courses JSON.
     * Returns response body as String on 2xx status codes, null otherwise.
     * Does not throw on non-2xx responses.
     */
    public String getCoursesJson() {
        if (AUTH_TOKEN == null || AUTH_TOKEN.isBlank()) {
            return null;
        }

        String url = baseURI + "courses?enrollment_state=active&per_page=100";

        try {
            HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(20))
                .header("Authorization", "Bearer " + AUTH_TOKEN)
                .header("Accept", "application/json")
                .GET()
                .build();

            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());

            if (res.statusCode() / 100 == 2) {
                return res.body();
            }
        } catch (Exception e) {
            // Silently handle errors, return null
        }

        return null;
    }

    /**
     * Performs GET request to fetch assignments JSON.
     * Returns response body as String on 2xx status codes, null otherwise.
     * Does not throw on non-2xx responses.
     */
    public String getAssignmentsJson() {
        if (AUTH_TOKEN == null || AUTH_TOKEN.isBlank()) {
            return null;
        }

        String url = baseURI + "courses/" + "self/assignments?per_page=100";

        try {
            HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(20))
                .header("Authorization", "Bearer " + AUTH_TOKEN)
                .header("Accept", "application/json")
                .GET()
                .build();

            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());

            if (res.statusCode() / 100 == 2) {
                return res.body();
            }
        } catch (Exception e) {
            // Silently handle errors, return null
        }

        return null;
    }

    /**
     * Performs GET request to fetch announcements JSON.
     * Returns response body as String on 2xx status codes, null otherwise.
     * Does not throw on non-2xx responses.
     */
    public String getAnnouncementsJson() {
        if (AUTH_TOKEN == null || AUTH_TOKEN.isBlank()) {
            return null;
        }

        String url = baseURI + "announcements?per_page=100";

        try {
            HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(20))
                .header("Authorization", "Bearer " + AUTH_TOKEN)
                .header("Accept", "application/json")
                .GET()
                .build();

            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());

            if (res.statusCode() / 100 == 2) {
                return res.body();
            }
        } catch (Exception e) {
            // Silently handle errors, return null
        }

        return null;
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
}
