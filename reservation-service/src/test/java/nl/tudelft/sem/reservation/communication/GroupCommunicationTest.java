package nl.tudelft.sem.reservation.communication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;

class GroupCommunicationTest {

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
    void isSecretaryOfUser() {
        boolean expected = true;
        String json = gson.toJson(expected);

        // set response content
        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn(json);
        boolean actual = GroupCommunication
                .isSecretaryOfUser(1L, 2L, token);
        assertEquals(expected, actual);
    }

    @Test
    void isSecretaryOfGroup() {
        boolean expected = true;
        String json = gson.toJson(expected);

        // set response content
        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn(json);
        boolean actual = GroupCommunication
                .isSecretaryOfGroup(1L, 2L, token);
        assertEquals(expected, actual);
    }

    @Test
    void isInGroup() {
        boolean expected = true;
        String json = gson.toJson(expected);

        // set response content
        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn(json);
        boolean actual = GroupCommunication
                .isInGroup(1L, 2L, token);
        assertEquals(expected, actual);
    }

    @Test
    void overlap() {
        boolean expected = true;
        String json = gson.toJson(expected);

        // set response content
        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn(json);
        boolean actual = GroupCommunication
                .overlap(1L, 2L, token);
        assertEquals(expected, actual);
    }
}