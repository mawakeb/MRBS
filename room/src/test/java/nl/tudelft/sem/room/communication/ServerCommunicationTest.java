package nl.tudelft.sem.room.communication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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


class ServerCommunicationTest {

    @Mock
    private transient HttpClient client;

    @Mock
    private transient HttpResponse<String> response;

    @Mock
    private transient HttpRequest mockRequest;


    @BeforeEach
    void setUp() throws IOException, InterruptedException {
        MockitoAnnotations.initMocks(this);

        // supply response mock for calls to client.send(request, bodyHandler)
        // also stores the corresponding request for access during tests
        when(client.send(any(HttpRequest.class), ArgumentMatchers.any()))
                .thenAnswer((InvocationOnMock invocation) -> response);

        ServerCommunication.setHttpClient(client);
    }

    @Test
    void requestHandler() {
        // send an empty request
        when(response.statusCode()).thenReturn(200);
        HttpResponse<String> result = ServerCommunication.requestHandler(mockRequest);

        // the httpClient responds without errors by default in @BeforeEach
        // so the method should simply return the mocked response
        assertEquals(response, result);
    }

    @Test
    void wrongRequestHandler() {
        // send an empty request
        when(response.statusCode()).thenReturn(401);
        HttpResponse<String> result = ServerCommunication.requestHandler(mockRequest);

        // the httpClient responds without errors by default in @BeforeEach
        // so the method should simply return the mocked response
        assertEquals(null, result);
    }
}