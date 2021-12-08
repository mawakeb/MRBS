package nl.tudelft.sem.room.exception;

/**
 * The UserNotFoundException.
 */
public class RoomNotFoundException extends Exception {

    public static final long serialVersionUID = 1L;

    /**
     * Instantiates a new Room not found exception.
     *
     * @param message the detail message.
     */
    public RoomNotFoundException(String message) {
        super("Room not found with id: " + message);
    }
}
