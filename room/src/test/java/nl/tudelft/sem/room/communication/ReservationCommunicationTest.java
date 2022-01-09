package nl.tudelft.sem.room.communication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;


class ReservationCommunicationTest {

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
    void getHi() {
        String expected = "hello_from_reservation";
        String json = gson.toJson(expected);

        // set response content
        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn(json);
        String actual = ReservationCommunication.getHi("token");
        assertEquals(expected, actual);
    }

    @Test
    void checkUserToReservation() {
        boolean expected = true;
        String json = gson.toJson(expected);

        // set response content
        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn(json);
        boolean actual = ReservationCommunication
                .checkUserToReservation(1L, 2L, token);
        assertEquals(expected, actual);
    }

    @Test
    void getRoomsInTimeslot() {
        List<Long> expected = List.of(1L, 2L, 3L);
        String json = gson.toJson(expected);

        // set response content
        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn(json);

        List<Long> rooms = List.of(1L, 2L, 3L, 4L, 5L);
        List<Long> actual = ReservationCommunication
                .getRoomsInTimeslot(rooms, "4", "6", token);
        assertEquals(expected, actual);
    }

    @Test
    void getRoomWithReservation() {
        Long expected = 2L;
        String json = gson.toJson(expected);

        // set response content
        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn(json);
        Long actual = ReservationCommunication
                .getRoomWithReservation(1L, token);
        assertEquals(expected, actual);
    }
}