package nl.tudelft.sem.room.communication;

import com.google.gson.reflect.TypeToken;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.List;

public class ReservationCommunication extends ServerCommunication {

    private static final String requestString = hostAddress + "/reservation";
    private static final String authorization = "Authorization";

    /**
     * Asks the reservation service if the given user is the user who made the reservation.
     *
     * @param userId id of the user
     * @param reservationId if of the reservation to compare to
     * @param token authentication token
     * @return boolean of the check
     */
    public static boolean checkUserToReservation(long userId, long reservationId, String token) {
        HttpRequest request = HttpRequest
                .newBuilder()
                .GET()
                .setHeader(authorization, token)
                .uri(URI.create(requestString + "/checkUser" + "?userId=" + userId
                        + "&reservationId=" + reservationId)).build();
        return gson
                .fromJson(requestHandler(request).body(), new TypeToken<Boolean>() {
                }.getType());
    }

    /**
     * Gets a list of rooms that are free of reservation in the given time and list of rooms.
     *
     * @param rooms list of rooms to check among
     * @param startTime start of the timeslot to check
     * @param endTime end of the timeslot to check
     * @param token authentication token of the user
     * @return list of room ids of the room within the timeslot
     */
    public static List<Long> getRoomsInTimeslot(List<Long> rooms,
                                                String startTime,
                                                String endTime,
                                                String token) {
        String roomList = "";
        for (Long room : rooms) {
            roomList += room + ",";
        }
        roomList = roomList.substring(0, roomList.length() - 1);

        HttpRequest request = HttpRequest
                .newBuilder()
                .GET()
                .setHeader(authorization, token)
                .uri(URI.create(requestString + "/checkTimeslot" + "?rooms=" + roomList
                        + "&startTime=" + startTime + "&endTime=" + endTime)).build();
        return gson
                .fromJson(requestHandler(request).body(),
                        new TypeToken<List<Long>>() {
                        }.getType());
    }

    /**
     * Gets id of the room that the given reservation took place in.
     *
     * @param reservationId id of the reservation
     * @param token authentication token of the user
     * @return id of the room
     */
    public static long getRoomWithReservation(long reservationId,
                                              String token) {
        HttpRequest request = HttpRequest.newBuilder()
                .setHeader(authorization, token)
                .GET().uri(URI.create(requestString + "/getRoom"
                        + "?id=" + reservationId)).build();
        return gson
                .fromJson(requestHandler(request).body(),
                        new TypeToken<Long>() {
                        }.getType());
    }

}

