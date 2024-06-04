package dev.greyferret.exception;

/**
 * Any exception while attempt to delete file
 */
public class FileAccessException extends RuntimeException {
    public FileAccessException(String message) {
        super(message);
    }
}
