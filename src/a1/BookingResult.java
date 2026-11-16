package a1;

/**
 * The outcome of an operation on the appointment collection: whether it worked
 * and a message to show the user.
 *
 * <p>Returning a small result object rather than printing inside the manager
 * keeps the manager free of output code: the {@code main} method decides how
 * the message is displayed, and the same result could later be shown in a GUI
 * without changing the manager at all (Part 5 design consideration: "what
 * feedback should the system give the user when an operation succeeds or
 * fails?").</p>
 */
public final class BookingResult {

    private final boolean successful;
    private final String message;

    private BookingResult(boolean successful, String message) {
        this.successful = successful;
        this.message = message;
    }

    /**
     * @param message what went well
     * @return a successful result
     */
    public static BookingResult success(String message) {
        return new BookingResult(true, message);
    }

    /**
     * @param message the warning that explains why the action could not be done
     * @return a failed result
     */
    public static BookingResult warning(String message) {
        return new BookingResult(false, message);
    }

    public boolean isSuccessful() {
        return successful;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return (successful ? "OK    " : "WARN  ") + message;
    }
}
