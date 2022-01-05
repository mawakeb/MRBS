package nl.tudelft.sem.user.exception;

/**
 * The UserNotFoundException.
 */
public class NetIdAlreadyExistsException extends Exception {

    public static final long serialVersionUID = -8397935910727922694L;

    /**
     * Instantiates a new User not found exception.
     *
     * @param message the detail message.
     */
    public NetIdAlreadyExistsException(String message) {
        super("User already exists with netID: " + message);
    }
}
