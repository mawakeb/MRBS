package nl.tudelft.sem.reservation.validators;

import nl.tudelft.sem.reservation.entity.Reservation;
import nl.tudelft.sem.reservation.exception.InvalidReservationException;

public abstract class BaseValidator implements Validator {
    private Validator next;

    public void setNext(Validator h){
        this.next = h;
    }

    /**
     * Runs check on the next object in chain or ends traversing if we're in
     * last object in chain.
     */
    protected boolean checkNext(Reservation reservation) throws InvalidReservationException {
        if (next == null) {
            return true;
        }
        return next.handle(reservation);
    }
}
