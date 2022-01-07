package nl.tudelft.sem.reservation.communication;

import com.google.gson.reflect.TypeToken;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.List;

public class UserCommunication extends ServerCommunication {

    private static final String requestString = hostAddress + "/user";

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

