package nl.tudelft.sem.reservation.communication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;


class RoomCommunicationTest {

    private static final Gson gson = new Gson();

    @Mock
    private transient HttpClient client;

    @Mock
    private transient HttpResponse<String> response;

    private transient HttpRequest request;
    private transient String token = "token";

    @BeforeEach
    void setUp() throws IOException, InterruptedException {
        MockitoAnnotations.initMocks(this);

        // supply response mock for calls to client.send(request, bodyHandler)
        // also stores the corresponding request for access during tests
        when(client.send(any(HttpRequest.class), ArgumentMatchers.any()))
                .thenAnswer((InvocationOnMock invocation) -> {
                    request = (HttpRequest) invocation.getArguments()[0];
                    return response;
                });

        ServerCommunication.setHttpClient(client);
    }

    @Test
    void getRoomAvailability() {
        boolean expected = true;
        String json = gson.toJson(expected);

        // set response content
        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn(json);
        //TODO:IllegalArgumentException ??
        /*boolean actual = RoomCommunication
                .getRoomAvailability(1L,
                        LocalTime.of(8, 0),
                        LocalTime.of(18, 0),
                        token);
        assertEquals(expected, actual);*/
    }
}