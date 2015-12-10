package com.jonbake.report.exception;

/**
 * {@link RuntimeException} that gets thrown when an invalid configuration value is encountered.
 *
 * @author jonmbake
 */
public class ConfigurationException extends ReportsException {
    /**
     * Construct with message.
     *
     * @param message - error message
     */
    public ConfigurationException (final String message) {
        super(message, "Configuration Error");
    }
}
