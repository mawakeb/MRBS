package nl.tudelft.sem.reservation;

public interface Validator {

    void setNext(Validator handler);
    boolean handle(Reservation reservation) throws InvalidReservationException;
}
