package nl.tudelft.sem.group.communication;

import com.google.gson.reflect.TypeToken;
import java.net.URI;
import java.net.http.HttpRequest;

public class UserCommunication extends ServerCommunication {

    private static final String requestString = hostAddress + "/user";

    public static Long getUser() {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(requestString + "/getCurrentUserID"))
                .build();
        return gson
                .fromJson(requestHandler(request)
                        .body(), new TypeToken<String>() {}
                        .getType());
    }

    public static String getUserType() {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(requestString + "/getCurrentUserType"))
                .build();
        return gson
                .fromJson(requestHandler(request)
                        .body(), new TypeToken<String>() {}
                        .getType());
    }
}
