package nl.tudelft.sem.reservation.communication;

import com.google.gson.reflect.TypeToken;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.List;

public class GroupCommunication extends ServerCommunication {

    private static final String requestString = hostAddress + "/group";

    public static boolean isSecretaryOfUser(Long secretaryId, Long employeeId) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(requestString + "/isSecretaryOfUser" + "?secretaryId=" + secretaryId + "&employeeId=" + employeeId))
                .build();
        return gson
                .fromJson(requestHandler(request)
                        .body(), new TypeToken<String>() {}
                        .getType());
    }

    public static boolean isSecretaryOfGroup(Long secretaryId, Long groupId) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(requestString + "/isSecretaryOfGroup" + "?secretaryId=" + secretaryId + "&groupId=" + groupId))
                .build();
        return gson
                .fromJson(requestHandler(request)
                        .body(), new TypeToken<String>() {}
                        .getType());
    }

    public static boolean isInGroup(Long userId, Long groupId) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(requestString + "/isInGroup" + "?userId=" + userId + "&groupId=" + groupId))
                .build();
        return gson
                .fromJson(requestHandler(request)
                        .body(), new TypeToken<String>() {}
                        .getType());
    }

    public static boolean overlap(Long groupId1, Long groupId2) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(requestString + "/overlap" + "?groupId1=" + groupId1 + "&groupId2=" + groupId2))
                .build();
        return gson
                .fromJson(requestHandler(request)
                        .body(), new TypeToken<String>() {}
                        .getType());
    }
}

