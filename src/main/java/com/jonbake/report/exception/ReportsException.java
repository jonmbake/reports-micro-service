package com.jonbake.report.exception;

/**
 * Base exception for any mapped runtime exception.
 *
 * @author jonmbake
 */
public class ReportsException extends RuntimeException {
    private final String type;
    /**
     * Construct with message and type.
     *
     * @param message - error message
     * @param type - type should describe class of error
     */
    public ReportsException (final String message, final String type) {
        super(message);
        this.type = type;
    }
    public final String getType () {
        return type;
    }
}
