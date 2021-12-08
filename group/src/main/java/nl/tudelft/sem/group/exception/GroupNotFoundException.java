package nl.tudelft.sem.group.exception;

/**
 * The UserNotFoundException.
 */
public class GroupNotFoundException extends Exception {

    public static final long serialVersionUID = 1L;

    /**
     * Instantiates a new Group not found exception.
     *
     * @param message the detail message.
     */
    public GroupNotFoundException(String message) {
        super("Room not found with id: " + message);
    }
}
