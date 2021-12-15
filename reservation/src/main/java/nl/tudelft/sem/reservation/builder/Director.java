package nl.tudelft.sem.reservation.builder;

import nl.tudelft.sem.reservation.builder.ReservationBuilder;
import nl.tudelft.sem.reservation.entity.ReservationType;

public class Director {
    public void buildSingleReservation(ReservationBuilder builder) {
        builder.type(ReservationType.SINGLE);
    }

    public void buildGroupReservation(ReservationBuilder builder, String purpose, Long groupId) {
        builder.group(groupId);
        builder.purpose(purpose);
        builder.type(ReservationType.GROUP);
    }
}