package nl.tudelft.sem.reservation.builder;

import nl.tudelft.sem.reservation.builder.ReservationBuilder;
import nl.tudelft.sem.reservation.entity.ReservationType;

public class Director {
    public void buildSelfReservation(ReservationBuilder builder) {
        builder.type(ReservationType.SELF);
    }

    public void buildAdminReservation(ReservationBuilder builder, Long forUser) {
        builder.type(ReservationType.ADMIN);
        builder.user(forUser);
    }

    public void buildGroupReservation(ReservationBuilder builder, Long forGroup, String purpose) {
        builder.type(ReservationType.GROUP);
        builder.group(forGroup);
        builder.purpose(purpose);
    }
}