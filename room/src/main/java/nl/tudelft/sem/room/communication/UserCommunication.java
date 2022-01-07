package nl.tudelft.sem.room.communication;

import com.google.gson.reflect.TypeToken;

import java.net.URI;
import java.net.http.HttpRequest;

public class UserCommunication extends ServerCommunication {

    private static final String requestString = hostAddress + "/user";


    public static String getHi(String token) {
        HttpRequest request = HttpRequest
                .newBuilder()
                .GET()
                .setHeader("Authorization", token)
                .uri(URI.create(requestString)).build();
        return gson
                .fromJson(requestHandler(request).body(), new TypeToken<String>() {}.getType());
    }

    public static String getRole(Long userId, String token) {
        HttpRequest request = HttpRequest
                .newBuilder()
                .GET()
                .setHeader("Authorization", token)
                .uri(URI.create(requestString+ "/getRole?userId=" + userId)).build();

        return gson
                .fromJson(requestHandler(request).body(), new TypeToken<String>() {}.getType());
    }

}
