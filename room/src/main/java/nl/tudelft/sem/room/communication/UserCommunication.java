package nl.tudelft.sem.room.communication;

import com.google.gson.reflect.TypeToken;
import java.net.URI;
import java.net.http.HttpRequest;

public class UserCommunication extends ServerCommunication {

    private static final String requestString = hostAddress + "/user";

    /**
     * Test method.
     *
     * @param token the authentication token of the user
     * @return hi is communicated successfully, status otherwise
     */
    public static String getHi(String token) {
        HttpRequest request = HttpRequest
                .newBuilder()
                .GET()
                .setHeader("Authorization", token)
                .uri(URI.create(requestString)).build();
        return gson
                .fromJson(requestHandler(request).body(), new TypeToken<String>() {
                }.getType());
    }

    /**
     * Gets role of user with given token.
     *
     * @param token authentication token of the user
     * @return the role of the user
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

    /**
     * Gets id of user with given token.
     *
     * @param token authentication token of the user
     * @return the id of the user
     */
    public static Long getUserId(String token) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .setHeader("Authorization", token)
                .uri(URI.create(requestString + "/getCurrentUserId"))
                .build();
        return gson
                .fromJson(requestHandler(request)
                        .body(), new TypeToken<Long>() {}
                        .getType());
    }



}
