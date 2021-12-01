package nl.tudelft.sem.reservation;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.UUID;

public class Reservation {

    private int id;
    private int roomID;
    private String userID;
    private LocalTime start;
    private LocalTime end;
    private String purpose;
    private String editPurpose;


    /**
     * Constructor for the Reservation class.
     *
     * @param id      unique room UUID.
     * @param roomID  title of the room chosen by lecturer.
     * @param userID  room access code for the staff role.
     * @param start   room access code for the student role.
     * @param end     room access code for the student role.
     * @param purpose room access code for the student role.
     */
    public Reservation(int id, int roomID, String userID, LocalTime start, LocalTime end, String purpose) {
        this.id = id;
        this.roomID = roomID;
        this.userID = userID;
        this.start = start;
        this.end = end;
        this.purpose = purpose;
    }
}
