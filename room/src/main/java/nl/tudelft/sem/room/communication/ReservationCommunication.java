package nl.tudelft.sem.room.communication;

import com.google.gson.reflect.TypeToken;
import nl.tudelft.sem.room.entity.Room;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.List;

public class ReservationCommunication extends ServerCommunication {

    private static final String requestString = hostAddress + "8081/reservation";


    public static String getHi() {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(requestString)).build();
        return gson
                .fromJson(requestHandler(request).body(), new TypeToken<String>() {}.getType());
    }

    public static boolean checkUserToReservation(String parsedList) {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(parsedList))
                .uri(URI.create(requestString + "/checkUser")).build();
        return gson
                .fromJson(requestHandler(request).body(), new TypeToken<Boolean>() {}.getType());
    }

    public static List<Room> getRoomsInTimeslot(List<Room> rooms, String startTime, String endTime) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(requestString + "/checkTimeslot")).build();
        return gson
                .fromJson(requestHandler(request).body(), new TypeToken<List<Room>>() {}.getType());
    }


    /*
    public static String getLectureById(Long lectureId) {
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create(requestString + "/getById?lectureId=" + lectureId)).build();
        return gson.fromJson(requestHandler(request).body(), new TypeToken<String>() {}.getType());
    }*/


}

