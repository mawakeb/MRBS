package nl.tudelft.sem.reservation.communication;

import com.google.gson.reflect.TypeToken;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.List;

public class RoomCommunication extends ServerCommunication {

    private static final String requestString = hostAddress + "8082/room";

    public static List<Long> getAllRoomIds() {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(requestString +
                "/getRoomIds")).build();
        return gson
                .fromJson(requestHandler(request).body(),
                        new TypeToken<List<Long>>() {}.getType());
    }
}

