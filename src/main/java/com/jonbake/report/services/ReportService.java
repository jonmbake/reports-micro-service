package com.jonbake.report.services;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 *  A service for running and obtaining information about a report.
 *
 * @author jonmbake
 * @param <T> - filled report type
 */
public interface ReportService<T> {
    /**
     * Get a list of available reports.
     *
     * @return - list of available reports
     */
    Set<String> getReportList ();
    /**
     * Run a report.  Will return {@link Optional#empty()} if report is not found or fails to run.
     *
     * @param reportName - name of report to run.
     * @param reportParams report parameters
     * @return - filled report if fill ran successfully
     */
    Optional<T> run (final String reportName, final Map<String, Object> reportParams);
}
