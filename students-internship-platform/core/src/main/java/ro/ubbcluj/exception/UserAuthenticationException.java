package ro.ubbcluj.exception;

/**
 * UserException is a class which contains all the
 * exceptions regarding User Management.
 */
public class UserAuthenticationException extends GenericException {

    /**
     * Default constructor.
     */
    public UserAuthenticationException() {
        super();
    }

    /**
     * Constructor.
     *
     * @param message
     */
    public UserAuthenticationException(String message) {
        super("User exception: " + message);
    }

    /**
     * Constructor.
     *
     * @param message
     * @param cause
     */
    public UserAuthenticationException(String message, Throwable cause) {
        super("User exception: " + message, cause);
    }

}
