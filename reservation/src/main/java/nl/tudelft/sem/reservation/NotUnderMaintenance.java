package nl.tudelft.sem.reservation;

public class NotUnderMaintenance extends BaseValidator {

    @Override
    public boolean handle(Reservation reservation) throws InvalidReservationException {

        return super.checkNext(reservation);
    }
}
