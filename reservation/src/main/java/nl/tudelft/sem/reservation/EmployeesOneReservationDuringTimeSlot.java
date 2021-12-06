package nl.tudelft.sem.reservation;

public class EmployeesOneReservationDuringTimeSlot extends BaseValidator {

    @Override
    public boolean handle(Reservation reservation) throws InvalidReservationException {

        return super.checkNext(reservation);
    }
}
