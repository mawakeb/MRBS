package nl.tudelft.sem.reservation.communication;

import com.google.gson.reflect.TypeToken;

import java.net.URI;
import java.net.http.HttpRequest;

public class RoomCommunication extends ServerCommunication {

    private static final String requestString = hostAddress + ":8082/room";

    public static String getBuildingOpeningHours(int roomID) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(requestString + "/getBuildingOpeningHours?roomID=" + roomID))
                .build();
        return gson
                .fromJson(requestHandler(request)
                        .body(), new TypeToken<String>() {}
                        .getType());
    }
}
