package nl.tudelft.sem.room.communication;

import com.google.gson.reflect.TypeToken;

import java.net.URI;
import java.net.http.HttpRequest;

public class ReservationCommunication extends ServerCommunication {

    private static final String requestString = hostAddress + "8081/reservation";


    public static String getHi() {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(requestString)).build();
        return gson
                .fromJson(requestHandler(request).body(), new TypeToken<String>() {}.getType());
    }

    public static boolean checkUserToReservation(long userId, long reservationId) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET().uri(URI.create(requestString + "/checkUser" + "?userId=" + userId +
                        "&reservationId=" + reservationId)).build();
        return gson
                .fromJson(requestHandler(request).body(), new TypeToken<Boolean>() {}.getType());
    }


    /*
    public static String getLectureById(Long lectureId) {
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create(requestString + "/getById?lectureId=" + lectureId)).build();
        return gson.fromJson(requestHandler(request).body(), new TypeToken<String>() {}.getType());
    }*/


}

