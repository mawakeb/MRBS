package nl.tudelft.sem.group.communication;

import com.google.gson.reflect.TypeToken;
import java.net.URI;
import java.net.http.HttpRequest;

public class UserCommunication extends ServerCommunication {

    private static final String requestString = hostAddress + "/user";
    private static final String authorization = "Authorization";

    /**
     * Get the type of user with the auth token.
     * Added to allow unit testing.
     *
     * @param token the authentication token of the user
     * @return the type of the user
     */
    public static String getCurrentUserType(String token) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .setHeader(authorization, token)
                .uri(URI.create(requestString + "/getCurrentUserType"))
                .build();
        return gson
                .fromJson(requestHandler(request)
                        .body(), new TypeToken<String>() {}
                        .getType());
    }

    /**
     * Get the type of user with the id.
     *
     * @param id the id of the requested user
     * @param token the authentication token of the current user
     * @return the type of the user
     */
    public static String getUserType(Long id, String token) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .setHeader(authorization, token)
                .uri(URI.create(requestString + "/getUserType"
                        + "?id=" + id))
                .build();
        return gson
                .fromJson(requestHandler(request)
                        .body(), new TypeToken<String>() {}
                        .getType());
    }

    /**
     * Set the type of user with the id.
     *
     * @param id the id of the requested user
     * @param type the new type for the requested user
     * @param token the authentication token of the current user
     * @return a status message
     */
    public static String setUserType(Long id, String type, String token) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .setHeader(authorization, token)
                .uri(URI.create(requestString + "/setUserType"
                        + "?id=" + id + "&type=" + type))
                .build();
        return gson
                .fromJson(requestHandler(request)
                        .body(), new TypeToken<String>() {}
                        .getType());
    }
}
