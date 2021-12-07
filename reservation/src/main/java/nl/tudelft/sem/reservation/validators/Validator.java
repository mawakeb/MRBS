package nl.tudelft.sem.reservation.validators;

import nl.tudelft.sem.reservation.entity.Reservation;
import nl.tudelft.sem.reservation.exception.InvalidReservationException;

public interface Validator {

    void setNext(Validator handler);

    boolean handle(Reservation reservation) throws InvalidReservationException;
}
