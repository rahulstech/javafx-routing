package rahulstech.jfx.routing.parser;

/**
 * The {@code ParserException} class represents an unchecked exception
 * that is thrown during the parsing process in the routing framework.
 *
 * <p>This exception can be used to signal various issues encountered
 * during parsing, such as invalid syntax, missing required elements,
 * or other runtime errors that occur while processing routing configurations.</p>
 */
public class ParserException extends RuntimeException {

    /**
     * Constructs a new {@code ParserException} with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a call to {@link #initCause}.
     */
    public ParserException() {
    }

    /**
     * Constructs a new {@code ParserException} with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for later retrieval
     *                by the {@link #getMessage()} method.
     */
    public ParserException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code ParserException} with the specified detail message and cause.
     *
     * <p>Note that the detail message associated with {@code cause} is <i>not</i> automatically
     * incorporated into this exception's detail message.</p>
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method).
     *              (A {@code null} value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public ParserException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new {@code ParserException} with the specified cause and a detail message of
     * {@code (cause==null ? null : cause.toString())} (which typically contains the class and detail message of {@code cause}).
     * This constructor is useful for exceptions that are little more than wrappers for other throwables.
     *
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method).
     *              (A {@code null} value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public ParserException(Throwable cause) {
        super(cause);
    }
}
