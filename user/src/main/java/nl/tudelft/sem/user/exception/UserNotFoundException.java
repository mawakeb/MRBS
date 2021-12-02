package nl.tudelft.sem.user.exception;

/**
 * The UserNotFoundException.
 */
public class UserNotFoundException extends Exception {
    /**
     * Instantiates a new User not found exception.
     *
     * @param message the detail message.
     */
    public UserNotFoundException(String message) {
        super("User not found with id: " + message);
    }
}
