package nl.tudelft.sem.reservation;

public class InvalidReservationException extends Exception {

    public static final long serialVersionUID = 1L;

    public InvalidReservationException(String errorMessage) {
        super(errorMessage);
    }
}
