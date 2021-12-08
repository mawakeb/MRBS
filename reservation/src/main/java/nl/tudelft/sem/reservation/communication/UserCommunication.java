package nl.tudelft.sem.reservation.communication;

import com.google.gson.reflect.TypeToken;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.List;

public class UserCommunication extends ServerCommunication {

    private static final String requestString = hostAddress + "8083/user";

    public static Long getUser() {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(requestString + "/getCurrentUserID"))
                .build();
        return gson
                .fromJson(requestHandler(request)
                        .body(), new TypeToken<String>() {}
                        .getType());
    }

    public static String getUserType() {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(requestString + "/getCurrentUserType"))
                .build();
        return gson
                .fromJson(requestHandler(request)
                        .body(), new TypeToken<String>() {}
                        .getType());
    }

    public static List getTeamMembers(Long secretary) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(requestString + "/getTeamMemberIDs?secretaryUserID=" + secretary))
                .build();
        return gson
                .fromJson(requestHandler(request)
                        .body(), new TypeToken<String>() {}
                        .getType());
    }

    /*
    public static String getLectureById(Long lectureId) {
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create(requestString + "/getById?lectureId=" + lectureId)).build();
        return gson.fromJson(requestHandler(request).body(), new TypeToken<String>() {}.getType());
    }
    */
}

