package dev.greyferret.exception;

/**
 * Path parsing wrapper exception
 */
public class PathFileSyntaxException extends RuntimeException {
    public PathFileSyntaxException(String input, Exception ex) {
        super("Can't parse URI: " + input, ex);
    }
}
