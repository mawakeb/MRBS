package nl.tudelft.sem.reservation.builder;

import nl.tudelft.sem.reservation.entity.Reservation;
import nl.tudelft.sem.reservation.entity.ReservationType;

public interface Builder {


    /**
     * Adds a given type to the builder, to be used when building the reservation.
     *
     * @param type  The type of reservation to be added (SELF, SINGLE, ADMIN, or GROUP)
     */
    public void type(ReservationType type);

    /**
     * Adds a given user the reservation is for to the builder,
     * to be used when building the reservation.
     *
     * @param userId   A Long representing the user
     */
    public void user(Long userId);

    /**
     * Adds a given group the reservation is for to the builder,
     * to be used when building the reservation.
     *
     * @param groupId   A Long representing the group
     */
    public void group(Long groupId);

    /**
     * Adds a given purpose message to the builder, to be used when building the reservation.
     *
     * @param message   A string containing the purpose of the reservation
     */
    public void purpose(String message);


    /**
     * Get madeBy.
     *
     * @return  a Long representing the user trying to make the reservation the builder is for
     */
    public Long getMadeBy();


    /**
     * Builds the reservation with the given values.
     *
     * @return  A Reservation object with the attributes specified with above methods
     */
    public Reservation build();
}