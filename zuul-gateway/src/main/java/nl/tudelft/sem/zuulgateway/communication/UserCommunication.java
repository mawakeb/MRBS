package nl.tudelft.sem.zuulgateway.communication;

import com.google.gson.reflect.TypeToken;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class UserCommunication extends ServerCommunication {

    private static final String requestString = hostAddress + "/user";

    public static String getHi() {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(requestString)).build();
        HttpResponse<String> response = requestHandler(request);

        if (response.statusCode() != 200) System.out.println(response.statusCode());

        return gson
                .fromJson(response.body(), new TypeToken<String>() {}.getType());
    }
}
