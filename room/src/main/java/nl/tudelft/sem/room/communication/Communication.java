package nl.tudelft.sem.room.communication;

import com.google.gson.reflect.TypeToken;

import java.net.URI;
import java.net.http.HttpRequest;

public class Communication extends ServerCommunication {

    private static final String requestString = hostAddress + "/reservation";


    public static String getHi() {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(requestString)).build();
        return gson
                .fromJson(requestHandler(request).body(), new TypeToken<String>() {}.getType());
    }


    /*
    public static String getLectureById(Long lectureId) {
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create(requestString + "/getById?lectureId=" + lectureId)).build();
        return gson.fromJson(requestHandler(request).body(), new TypeToken<String>() {}.getType());
    }*/


}

