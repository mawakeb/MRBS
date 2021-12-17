package nl.tudelft.sem.reservation.builder;

import nl.tudelft.sem.reservation.entity.ReservationType;

@SuppressWarnings("PMD.BeanMembersShouldSerialize")
public class Director {
    private Builder builder;

    public Director(Builder builder) {
        this.builder = builder;
    }

    public void buildSelfReservation() {
        builder.type(ReservationType.SELF);
    }

    public void buildAdminReservation(Long forUser) {
        builder.type(ReservationType.ADMIN);
        builder.user(forUser);
    }

    public void buildGroupReservation(Long forGroup, String purpose) {
        builder.type(ReservationType.GROUP);
        builder.group(forGroup);
        builder.purpose(purpose);
    }
}