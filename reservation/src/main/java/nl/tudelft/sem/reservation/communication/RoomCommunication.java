package nl.tudelft.sem.reservation.communication;

import com.google.gson.reflect.TypeToken;
import java.net.URI;
import java.net.http.HttpRequest;
import java.time.LocalTime;

public class RoomCommunication extends ServerCommunication {

    private static final String requestString = hostAddress + "/room";

    /**
     * Get if a given room is available in a given time frame.
     *
     * @param roomId the id of the room
     * @param start the start of the time frame
     * @param end the end of the time frame
     * @param token an authorization token
     * @return if the room is available
     */
    public static boolean getRoomAvailability(long roomId, LocalTime start,
                                              LocalTime end, String token) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .setHeader("Authorization", token)
                .uri(URI.create(requestString + "/checkAvailable" + "?roomId=" + roomId
                        + "&start=" + start + "&end=" + end)).build();
        return gson
                .fromJson(requestHandler(request)
                        .body(), new TypeToken<Boolean>() {}
                        .getType());
    }
}
