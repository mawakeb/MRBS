package nl.tudelft.sem.reservation;

import java.time.ZonedDateTime;
import java.util.UUID;

public class Reservation {

    private UUID id;
    private String name;

    

    /**
     * Constructor for the Room class.
     *
     * @param id      unique room UUID.
     * @param name    title of the room chosen by lecturer.
     * @param staff   room access code for the staff role.
     * @param student room access code for the student role.
     */
    public Reservation(UUID id, String name, String staff, String student, ZonedDateTime openTime) {
        this.id = id;
        this.name = name;
        this.staff = staff;
        this.student = student;
        this.isOpen = true;
        this.openTime = openTime;
        this.slowModeSeconds = 0;
        this.studentSize = 0;
    }


}
