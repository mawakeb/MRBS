package nl.tudelft.sem.reservation;

import com.google.gson.reflect.TypeToken;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.List;

public class Communication extends nl.tudelft.sem.reservation.ServerCommunication {

    private static final String requestString = hostAddress + "/lecture";

    /**
     * Gets all lectures.
     *
     * @return all lectures in the database
     */
    public static List<Lecture> getAllLectures() {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(requestString)).build();
        return gson
                .fromJson(requestHandler(request).body(), new TypeToken<List<Lecture>>() {}.getType());
    }

    /**
     * Gets lecture by id.
     *
     * @param lectureId the lecture id
     * @return the lecture by id
     */
    public static Lecture getLectureById(Long lectureId) {
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create(requestString + "/getById?lectureId=" + lectureId)).build();
        return gson.fromJson(requestHandler(request).body(), new TypeToken<Lecture>() {}.getType());
    }


}

