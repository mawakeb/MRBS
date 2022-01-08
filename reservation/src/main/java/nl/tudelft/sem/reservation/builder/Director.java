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
        builder.user(builder.getMadeBy());
    }

    public void buildSingleReservation(Long forUser, Long ofGroup, String purpose) {
        builder.type(ReservationType.SINGLE);
        builder.user(forUser);
        builder.group(ofGroup);
        builder.purpose(purpose);
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