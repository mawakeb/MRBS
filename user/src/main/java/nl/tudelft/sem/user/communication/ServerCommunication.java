package nl.tudelft.sem.user.communication;

import com.google.gson.Gson;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ServerCommunication {
    protected static String hostAddress = "http://localhost:";
    protected static HttpClient client = HttpClient.newBuilder().build();
    protected static Gson gson = new Gson();

    protected static HttpResponse<String> requestHandler(HttpRequest request) {
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int statusOK = 200;
            if (response.statusCode() != statusOK) {
                System.out.println("Status: " + response.statusCode());
                return null;
            }
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
