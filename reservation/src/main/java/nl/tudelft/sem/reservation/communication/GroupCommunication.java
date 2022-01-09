package nl.tudelft.sem.reservation.communication;

import com.google.gson.reflect.TypeToken;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.List;

@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class GroupCommunication extends ServerCommunication {

    private static final String requestString = hostAddress + "/group";

    public static boolean isSecretaryOfUser(Long secretaryId, Long employeeId, String token) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .setHeader("Authorization", token)
                .uri(URI.create(requestString + "/isSecretaryOfUser" + "?secretaryId="
                        + secretaryId + "&employeeId=" + employeeId))
                .build();
        return gson
                .fromJson(requestHandler(request)
                        .body(), new TypeToken<String>() {}
                        .getType());
    }

    public static boolean isSecretaryOfGroup(Long secretaryId, Long groupId, String token) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .setHeader("Authorization", token)
                .uri(URI.create(requestString + "/isSecretaryOfGroup" + "?secretaryId="
                        + secretaryId + "&groupId=" + groupId))
                .build();
        return gson
                .fromJson(requestHandler(request)
                        .body(), new TypeToken<String>() {}
                        .getType());
    }

    public static boolean isInGroup(Long userId, Long groupId, String token) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .setHeader("Authorization", token)
                .uri(URI.create(requestString + "/isInGroup" + "?userId="
                        + userId + "&groupId=" + groupId))
                .build();
        return gson
                .fromJson(requestHandler(request)
                        .body(), new TypeToken<String>() {}
                        .getType());
    }

    public static boolean overlap(Long groupId1, Long groupId2, String token) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .setHeader("Authorization", token)
                .uri(URI.create(requestString + "/overlap" + "?groupId1="
                        + groupId1 + "&groupId2=" + groupId2))
                .build();
        return gson
                .fromJson(requestHandler(request)
                        .body(), new TypeToken<String>() {}
                        .getType());
    }
}

