package csusm.cougarplanner;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.http.HttpClient;
import java.time.Duration;

public class API {

    private HttpClient client = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .followRedirects(HttpClient.Redirect.NORMAL)
        .connectTimeout(Duration.ofSeconds(3))
        .build();

    /**
     * Checks for an active internet connection.
     * This method is intended to verify network connectivity before making API calls.
     *
     * @return true if an internet connection is available, false otherwise
     */
    public boolean hasInternetConnection() {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("8.8.8.8", 53), 2000);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return false;
    }
}
