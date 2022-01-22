package nl.tudelft.sem.reservation.communication;

import com.google.gson.reflect.TypeToken;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.List;

@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class GroupCommunication extends ServerCommunication {

    private static final String requestString = hostAddress + "/group";

    /**
     * Check if a secretary is a secretary of a group containing a certain user.
     *
     * @param secretaryId the id of the secretary
     * @param employeeId the id of the user
     * @param token an authorization token
     * @return if the secretary is a secretary of this user
     */
    public static boolean isSecretaryOfUser(Long secretaryId, Long employeeId, String token) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .setHeader("Authorization", token)
                .uri(URI.create(requestString + "/isSecretaryOfUser" + "?secretaryId="
                        + secretaryId + "&employeeId=" + employeeId))
                .build();
        return gson
                .fromJson(requestHandler(request)
                        .body(), new TypeToken<Boolean>() {}
                        .getType());
    }

    /**
     * Check if a user is a secretary of a given group.
     *
     * @param secretaryId the id of the secretary
     * @param groupId the id of the group
     * @param token an authorization token
     * @return if the user is a secretary of the group
     */
    public static boolean isSecretaryOfGroup(Long secretaryId, Long groupId, String token) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .setHeader("Authorization", token)
                .uri(URI.create(requestString + "/isSecretaryOfGroup" + "?secretaryId="
                        + secretaryId + "&groupId=" + groupId))
                .build();
        return gson
                .fromJson(requestHandler(request)
                        .body(), new TypeToken<Boolean>() {}
                        .getType());
    }

    /**
     * Check if a user is in a group.
     *
     * @param userId the id of the user
     * @param groupId the id of the group
     * @param token an authorization token
     * @return if the user is in the group
     */
    public static boolean isInGroup(Long userId, Long groupId, String token) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .setHeader("Authorization", token)
                .uri(URI.create(requestString + "/isInGroup" + "?userId="
                        + userId + "&groupId=" + groupId))
                .build();
        return gson
                .fromJson(requestHandler(request)
                        .body(), new TypeToken<Boolean>() {}
                        .getType());
    }

    /**
     * Check if 2 groups overlap.
     *
     * @param groupId1 the id of the first group
     * @param groupId2 the id of the second group
     * @param token an authorization token
     * @return if the groups overlap
     */
    public static boolean overlap(Long groupId1, Long groupId2, String token) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .setHeader("Authorization", token)
                .uri(URI.create(requestString + "/overlap" + "?groupId1="
                        + groupId1 + "&groupId2=" + groupId2))
                .build();
        return gson
                .fromJson(requestHandler(request)
                        .body(), new TypeToken<Boolean>() {}
                        .getType());
    }
}

