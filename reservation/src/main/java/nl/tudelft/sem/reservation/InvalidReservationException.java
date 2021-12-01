package nl.tudelft.sem.reservation;

public class InvalidReservationException extends Exception {

    public InvalidReservationException(String errorMessage) {
        super(errorMessage);
    }
}
