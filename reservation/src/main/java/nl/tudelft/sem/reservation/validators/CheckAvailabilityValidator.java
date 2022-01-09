package nl.tudelft.sem.reservation.validators;

import com.google.gson.Gson;
import java.time.LocalTime;
import nl.tudelft.sem.reservation.communication.RoomCommunication;
import nl.tudelft.sem.reservation.entity.Reservation;
import nl.tudelft.sem.reservation.exception.InvalidReservationException;

public class CheckAvailabilityValidator extends BaseValidator {

    protected static Gson gson = new Gson();

    @Override
    public boolean handle(Reservation reservation, String token)
            throws InvalidReservationException {

        Long roomId = reservation.getRoomId();

        boolean availability = getRoomAvailability(roomId,
                reservation.getStart().toLocalTime(),
                reservation.getEnd().toLocalTime(),
                token);

        if (!availability) {
            throw new InvalidReservationException("The room is not available.");
        }

        return super.checkNext(reservation, token);
    }

    /**
     * Check if the room is available at given time.
     * Added to allow unit testing.
     *
     * @param roomId id of room to check
     * @param start start of the timeslot to check
     * @param end end of the timeslot to check
     * @param token authentication token of the user
     * @return true if room is available, false if otherwise
     */
    boolean getRoomAvailability(Long roomId, LocalTime start, LocalTime end, String token) {
        return RoomCommunication.getRoomAvailability(roomId, start, end, token);
    }
}
