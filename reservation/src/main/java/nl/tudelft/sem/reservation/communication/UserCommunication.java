package nl.tudelft.sem.reservation.communication;

import com.google.gson.reflect.TypeToken;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.List;

public class UserCommunication extends ServerCommunication {

    private static final String requestString = hostAddress + "/user";


    public static List<String> getAllLectures() {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(requestString)).build();
        return gson
                .fromJson(requestHandler(request).body(),
                        new TypeToken<List<String>>() {}.getType());
    }


    public static String getLectureById(Long lectureId) {
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create(requestString + "/getById?lectureId=" + lectureId)).build();
        return gson.fromJson(requestHandler(request).body(), new TypeToken<String>() {}.getType());
    }


}

