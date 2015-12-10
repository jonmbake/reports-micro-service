package com.jonbake.report.exception;

/**
 * {@link RuntimeException} thrown due to an unexpected error while running a report.
 *
 * @author jonmbake
 */
public class ReportRunningException extends ReportsException {
    private static final String DEFAULT_MESSAGE = "There was an unexpected error when attempting to run the report.";
    /**
     * Construct with message.
     *
     * @param message - error message
     */
    public ReportRunningException(final String message) {
        super(message, "Report Running Error");
    }
    /**
     * Construct with default error message.
     */
    public ReportRunningException() {
        this(DEFAULT_MESSAGE);
    }
}
