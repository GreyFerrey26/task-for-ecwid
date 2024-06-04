package dev.greyferret.exception;

/**
 * Generic Arguments Parsing Exception
 */
public class InputArgumentFormatException extends RuntimeException {
    public InputArgumentFormatException(String message, Exception ex) {
        super(message, ex);
    }

    public InputArgumentFormatException(String message) {
        super(message);
    }
}
