package nl.tudelft.sem.reservation.validators;

import nl.tudelft.sem.reservation.entity.Reservation;
import nl.tudelft.sem.reservation.exception.InvalidReservationException;

import nl.tudelft.sem.reservation.validators.BaseValidator;

public class WithinOpeningHoursValidator extends BaseValidator {

    @Override
    public boolean handle(Reservation reservation) throws InvalidReservationException {

        int roomID = reservation.getRoomId();

        //make http request for room building info from roomid


        return super.checkNext(reservation);
    }
}
