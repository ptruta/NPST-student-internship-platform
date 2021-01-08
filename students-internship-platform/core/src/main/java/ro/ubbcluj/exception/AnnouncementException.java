package ro.ubbcluj.exception;

/**
 * AnnouncementException is a class which contains all the
 * exceptions regarding Announcement Management.
 */
public class AnnouncementException extends GenericException {

    /**
     * Default constructor.
     */
    public AnnouncementException() {
        super();
    }

    /**
     * Constructor.
     *
     * @param message
     */
    public AnnouncementException(String message) {
        super("Announcement exception: " + message);
    }

    /**
     * Constructor.
     *
     * @param message
     * @param cause
     */
    public AnnouncementException(String message, Throwable cause) {
        super("Announcement exception: " + message, cause);
    }
}
