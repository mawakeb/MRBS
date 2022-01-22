package nl.tudelft.sem.reservation.builder;

import nl.tudelft.sem.reservation.entity.ReservationType;

@SuppressWarnings("PMD.BeanMembersShouldSerialize")
public class Director {

    private Builder builder;

    /**
     * Constructor for the Director class.
     *
     * @param builder   A builder object, which is used to keep track of the
     *                  attributes the Reservation should have
     */
    public Director(Builder builder) {
        this.builder = builder;
    }

    /**
     * Build a reservation of type SELF.
     */
    public void buildSelfReservation() {
        builder.type(ReservationType.SELF);
        builder.user(builder.getMadeBy());
    }

    /**
     * Build a reservation of type SINGLE.
     *
     * @param forUser   A Long representing the user the reservation is for
     * @param ofGroup   This kind of reservation must be made by a secretary of one of the
     *                  given user's research groups.
     *                  This field indicates for which research group that holds
     * @param purpose   A string explaining why the reservation was made
     */
    public void buildSingleReservation(Long forUser, Long ofGroup, String purpose) {
        builder.type(ReservationType.SINGLE);
        builder.user(forUser);
        builder.group(ofGroup);
        builder.purpose(purpose);
    }

    /**
     * Build a reservation of type ADMIN.
     *
     * @param forUser   A Long representing the user the reservation is for
     */
    public void buildAdminReservation(Long forUser) {
        builder.type(ReservationType.ADMIN);
        builder.user(forUser);
    }

    /**
     * Build a reservation of type GROUP.
     *
     * @param forGroup  A Long representing the research group the reservation is for
     * @param purpose   A string explaining why the reservation was made
     */
    public void buildGroupReservation(Long forGroup, String purpose) {
        builder.type(ReservationType.GROUP);
        builder.group(forGroup);
        builder.purpose(purpose);
    }

    public Builder getBuilder() {
        return builder;
    }
}