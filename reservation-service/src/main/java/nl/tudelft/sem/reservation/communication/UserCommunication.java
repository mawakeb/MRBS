package nl.tudelft.sem.reservation.communication;

import com.google.gson.reflect.TypeToken;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.List;

public class UserCommunication extends ServerCommunication {

    private static final String requestString = hostAddress + "/user";

    /**
     * Get the id of user with the auth token.
     * Added to allow unit testing.
     *
     * @param token the authentication token of the user
     * @return the id of the user
     */

    public static Long getUser(String token) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .setHeader("Authorization", token)
                .uri(URI.create(requestString + "/getCurrentUserID"))
                .build();
        return gson
                .fromJson(requestHandler(request)
                        .body(), new TypeToken<String>() {}
                        .getType());
    }

    /**
     * Get the type of user with the auth token.
     * Added to allow unit testing.
     *
     * @param token the authentication token of the user
     * @return the type of the user
     */

    public static String getUserType(String token) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .setHeader("Authorization", token)
                .uri(URI.create(requestString + "/getCurrentUserType"))
                .build();
        return gson
                .fromJson(requestHandler(request)
                        .body(), new TypeToken<String>() {}
                        .getType());
    }
}

