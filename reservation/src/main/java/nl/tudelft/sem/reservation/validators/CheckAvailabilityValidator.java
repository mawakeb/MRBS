package nl.tudelft.sem.reservation.validators;

import nl.tudelft.sem.reservation.communication.RoomCommunication;
import nl.tudelft.sem.reservation.entity.Reservation;
import nl.tudelft.sem.reservation.exception.InvalidReservationException;

import java.util.List;
import com.google.gson.Gson;

public class CheckAvailabilityValidator extends BaseValidator {
    protected static Gson gson = new Gson();

    @Override
    public boolean handle(Reservation reservation) throws InvalidReservationException {

        int roomID = reservation.getRoomId();

        List<String> sendList = List.of(
                Long.toString(roomID),
                reservation.getStart().toLocalTime().toString(),
                reservation.getEnd().toLocalTime().toString()
                );
        String parsedList = gson.toJson(sendList);

        boolean availability = RoomCommunication.getRoomAvailability(parsedList);

        if (!availability) {
            throw new InvalidReservationException("The room is not available");
        }

        return super.checkNext(reservation);
    }
}
