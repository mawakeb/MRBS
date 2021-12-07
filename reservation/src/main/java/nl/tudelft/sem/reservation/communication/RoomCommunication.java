package nl.tudelft.sem.reservation.communication;

import com.google.gson.reflect.TypeToken;

import java.net.URI;
import java.net.http.HttpRequest;

public class RoomCommunication extends ServerCommunication {

    private static final String requestString = hostAddress + ":8082/room";

    public static boolean getRoomAvailability(String list) {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(list))
                .uri(URI.create(requestString + "/checkAvailable"))
                .build();
        return gson
                .fromJson(requestHandler(request)
                        .body(), new TypeToken<Boolean>() {}
                        .getType());
    }
}
