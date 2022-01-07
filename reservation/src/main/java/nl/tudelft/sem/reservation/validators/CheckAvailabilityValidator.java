package nl.tudelft.sem.reservation.validators;

import nl.tudelft.sem.reservation.communication.RoomCommunication;
import nl.tudelft.sem.reservation.entity.Reservation;
import nl.tudelft.sem.reservation.exception.InvalidReservationException;

import com.google.gson.Gson;

public class CheckAvailabilityValidator extends BaseValidator {

    protected static Gson gson = new Gson();

    @Override
    public boolean handle(Reservation reservation, String token) throws InvalidReservationException {

        Long roomID = reservation.getRoomId();

        boolean availability = RoomCommunication.getRoomAvailability(roomID,
                reservation.getStart().toLocalTime(),
                reservation.getEnd().toLocalTime(),
                token);

        if (!availability) {
            throw new InvalidReservationException("The room is not available");
        }

        return super.checkNext(reservation, token);
    }
}
