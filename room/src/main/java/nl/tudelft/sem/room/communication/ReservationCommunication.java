package nl.tudelft.sem.room.communication;

import com.google.gson.reflect.TypeToken;
import nl.tudelft.sem.room.entity.Room;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.List;

public class ReservationCommunication extends ServerCommunication {

    private static final String requestString = hostAddress + "/reservation";

    public static String getHi(String token) {
        HttpRequest request = HttpRequest
                .newBuilder()
                .GET()
                .setHeader("Authorization", token)
                .uri(URI.create(requestString))
                .build();
        return gson
                .fromJson(requestHandler(request).body(), new TypeToken<String>() {}.getType());
    }

    public static boolean checkUserToReservation(long userId, long reservationId, String token) {
        HttpRequest request = HttpRequest
                .newBuilder()
                .GET()
                .setHeader("Authorization", token)
                .uri(URI.create(requestString + "/checkUser" + "?userId=" + userId +
                        "&reservationId=" + reservationId)).build();
        return gson
                .fromJson(requestHandler(request).body(), new TypeToken<Boolean>() {}.getType());
    }

    public static List<Long> getRoomsInTimeslot(List<Long> rooms, String startTime, String endTime, String token) {
        HttpRequest request = HttpRequest
                .newBuilder()
                .GET()
                .setHeader("Authorization", token)
                .uri(URI.create(requestString + "/checkTimeslot" + "?rooms=" + rooms +
                        "&startTime=" + startTime + "&endTime=" + endTime)).build();
        return gson
                .fromJson(requestHandler(request).body(),
                        new TypeToken<List<Long>>() {}.getType());
    }

    public static long getRoomWithReservation(long reservationId) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET().uri(URI.create(requestString + "/getRoom" + "?id=" + reservationId)).build();
        return gson
                .fromJson(requestHandler(request).body(),
                        new TypeToken<Long>() {}.getType());
    }

}

