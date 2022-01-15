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
        HttpRequest requestRoom = HttpRequest.newBuilder()
                .GET()
                .setHeader("Authorization", token)
                .uri(URI.create(requestString + "/checkAvailable" + "?roomId=" + roomId))
                        .build();

        boolean roomAvailability = gson
                .fromJson(requestHandler(requestRoom)
                        .body(), new TypeToken<Boolean>() {}
                        .getType());

        if (!roomAvailability) {
            return false;
        }

        HttpRequest buildingId = HttpRequest.newBuilder()
                .GET()
                .setHeader("Authorization", token)
                .uri(URI.create(requestString + "/getBuildingId" + "?roomId=" + roomId))
                .build();

        HttpRequest requestBuilding = HttpRequest.newBuilder()
                .GET()
                .setHeader("Authorization", token)
                .uri(URI.create(requestString + "/building/checkAvailable"
                        + "?buildingId=" + buildingId
                        + "&start=" + start + "&end=" + end)).build();

        boolean buildingAvailability = gson
                .fromJson(requestHandler(requestBuilding)
                        .body(), new TypeToken<Boolean>() {}
                        .getType());

        return buildingAvailability;
    }
}
