package nl.tudelft.sem.reservation;

import java.time.LocalDateTime;

public class Reservation {

    private int id;
    private int roomID;
    private String userID;
    private LocalDateTime start;
    private LocalDateTime end;
    private String purpose;
    private String editPurpose;
    private boolean cancelled;


    /**
     * Constructor for the Reservation class.
     *
     * @param id      unique reservation ID.
     * @param roomID  location of the reservation.
     * @param userID  netID of the user who made the reservation.
     * @param start   Date and time of the start of the reservation
     * @param end     Date and time of the end of the reservation
     * @param purpose purpose of the reservation
     */
    public Reservation(int id, int roomID, String userID, LocalDateTime start, LocalDateTime end, String purpose) {
        this.id = id;
        this.roomID = roomID;
        this.userID = userID;
        this.start = start;
        this.end = end;
        this.purpose = purpose;
        this.cancelled = false;
    }

    public void changeLocation(int newRoomID, String editPurpose){
        this.roomID = newRoomID;
        this.editPurpose = editPurpose;
    }

    public void changeTime(LocalDateTime newStart, LocalDateTime newEnd, String editPurpose){
        this.start = newStart;
        this.end = newEnd;
        this.editPurpose = editPurpose;
    }

    public void cancelReservation(int newRoomID, String editPurpose){
        this.cancelled = true;
        this.editPurpose = editPurpose;
    }
}
