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
     * @param token an authorization token
     * @return if the room is available
     */
    public static boolean getRoomAvailability(long roomId, String token) {
        HttpRequest requestRoom = HttpRequest.newBuilder()
                .GET()
                .setHeader("Authorization", token)
                .uri(URI.create(requestString + "/checkAvailable" + "?roomId=" + roomId))
                        .build();

        return gson
                .fromJson(requestHandler(requestRoom)
                        .body(), new TypeToken<Boolean>() {}
                        .getType());
    }

    /**
     * Get if a given building is available in a given time frame.
     *
     * @param buildingId the id of the building
     * @param start the start of the time frame
     * @param end the end of the time frame
     * @param token an authorization token
     * @return if the room is available
     */
    public static boolean getBuildingAvailability(long buildingId, LocalTime start,
                                              LocalTime end, String token) {

        HttpRequest requestBuilding = HttpRequest.newBuilder()
                .GET()
                .setHeader("Authorization", token)
                .uri(URI.create(requestString + "/building/checkAvailable"
                        + "?buildingId=" + buildingId
                        + "&start=" + start + "&end=" + end)).build();

        return gson
                .fromJson(requestHandler(requestBuilding)
                        .body(), new TypeToken<Boolean>() {}
                        .getType());
    }

    /**
     * Get id of the building with the roomId.
     *
     * @param roomId the id of the room
     * @param token an authorization token
     * @return the building id of the room
     */
    public static long getBuildingId(long roomId, String token) {

        HttpRequest building = HttpRequest.newBuilder()
                .GET()
                .setHeader("Authorization", token)
                .uri(URI.create(requestString + "/getBuildingId"
                        + "?roomId=" + roomId))
                .build();

        return gson
                .fromJson(requestHandler(building)
                        .body(), new TypeToken<Long>() {}
                        .getType());
    }
}
