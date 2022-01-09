package nl.tudelft.sem.reservation.communication;


import nl.tudelft.sem.reservation.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ReservationControllerTest {

    @Mock
    transient ReservationRepository reservationRepository;

    @BeforeEach
    void setUp() {

    }
}
