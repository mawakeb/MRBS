package nl.tudelft.sem.reservation.communication;

import com.google.gson.reflect.TypeToken;

import java.net.URI;
import java.net.http.HttpRequest;
import java.time.LocalTime;

public class RoomCommunication extends ServerCommunication {

    private static final String requestString = hostAddress + ":8082/room";

    public static boolean getRoomAvailability(long roomId, LocalTime start, LocalTime end) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(requestString + "/checkAvailable" + "?roomId=" + roomId +
                        "&start=" + start + "&end=" + end)).build();
        return gson
                .fromJson(requestHandler(request)
                        .body(), new TypeToken<Boolean>() {}
                        .getType());
    }
}
