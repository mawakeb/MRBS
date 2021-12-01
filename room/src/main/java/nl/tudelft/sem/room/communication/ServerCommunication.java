package nl.tudelft.sem.room.communication;

import com.google.gson.Gson;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ServerCommunication {
    protected static String hostAddress = "http://localhost:8081";
    protected static HttpClient client = HttpClient.newBuilder().build();
    protected static Gson gson = new Gson();

    protected static HttpResponse<String> requestHandler(HttpRequest request) {
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
            return null;
        }
        return response;
    }
}
