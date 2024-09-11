package rahulstech.jfx.routing.parser;

/**
 * The {@code ConverterException} class represents a specialized exception
 * that is thrown when an error occurs during the conversion process within the routing framework.
 *
 * <p>This exception extends {@link ParserException} and is used specifically for signaling
 * issues related to data conversion, such as when a string fails to convert to a desired type
 * or format during parsing.</p>
 */
public class ConverterException extends ParserException {

    /**
     * Constructs a new {@code ConverterException} with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a call to {@link #initCause}.
     */
    public ConverterException() {
    }

    /**
     * Constructs a new {@code ConverterException} with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for later retrieval
     *                by the {@link #getMessage()} method.
     */
    public ConverterException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code ConverterException} with the specified detail message and cause.
     *
     * <p>Note that the detail message associated with {@code cause} is <i>not</i> automatically
     * incorporated into this exception's detail message.</p>
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method).
     *              (A {@code null} value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public ConverterException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new {@code ConverterException} with the specified cause and a detail message of
     * {@code (cause==null ? null : cause.toString())} (which typically contains the class and detail message of {@code cause}).
     * This constructor is useful for exceptions that are little more than wrappers for other throwable.
     *
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method).
     *              (A {@code null} value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public ConverterException(Throwable cause) {
        super(cause);
    }
}
