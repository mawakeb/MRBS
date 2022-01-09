package nl.tudelft.sem.group.communication;

import com.google.gson.reflect.TypeToken;
import java.net.URI;
import java.net.http.HttpRequest;

public class UserCommunication extends ServerCommunication {

    private static final String requestString = hostAddress + "/user";
    private static final String authorization = "Authorization";


    public static Long getUser(String token) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .setHeader(authorization, token)
                .uri(URI.create(requestString + "/getCurrentUserID"))
                .build();
        return gson
                .fromJson(requestHandler(request)
                        .body(), new TypeToken<String>() {}
                        .getType());
    }

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

    public static String setUserType(Long id, String type, String token) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .setHeader(authorization, token)
                .uri(URI.create(requestString + "/getUserType"
                        + "?id=" + id + "&type=" + type))
                .build();
        return gson
                .fromJson(requestHandler(request)
                        .body(), new TypeToken<String>() {}
                        .getType());
    }
}
