package nl.tudelft.sem.reservation.validators;

import java.time.LocalDateTime;
import java.time.ZoneId;
import nl.tudelft.sem.reservation.entity.Reservation;
import nl.tudelft.sem.reservation.exception.InvalidReservationException;

public class EmployeesCannotReserveMoreThanTwoWeeksInAdvance
        extends BaseValidator {

    @Override
    public boolean handle(Reservation reservation, String token)
            throws InvalidReservationException {

        ZoneId zoneId = ZoneId.of("Europe/Amsterdam");
        LocalDateTime now = LocalDateTime.now(zoneId);
        LocalDateTime limit = now.plusWeeks(2);

        if (reservation.getEnd().isBefore(limit)) {
            return super.checkNext(reservation, token);
        }

        throw new InvalidReservationException("Reservation exceeds two-week limit.");
    }
}
