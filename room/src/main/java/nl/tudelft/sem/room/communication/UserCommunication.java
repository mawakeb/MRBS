package nl.tudelft.sem.room.communication;

import com.google.gson.reflect.TypeToken;

import java.net.URI;
import java.net.http.HttpRequest;

public class UserCommunication extends ServerCommunication {

    private static final String requestString = hostAddress + "/user";


    public static String getHi() {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(requestString)).build();
        return gson
                .fromJson(requestHandler(request).body(), new TypeToken<String>() {}.getType());
    }

    public static String getRole(Long userId) {
        HttpRequest request =
                HttpRequest.newBuilder().GET().uri(URI.create(requestString+ "/getRole?userId=" + userId)).build();

        return gson
                .fromJson(requestHandler(request).body(), new TypeToken<String>() {}.getType());
    }

}
