package com.jonbake.report.services;

import com.google.common.io.Files;
import com.jonbake.report.exception.ConfigurationException;
import com.jonbake.report.configuration.Configuration;
import com.jonbake.report.configuration.EnvironmentConfiguration;
import com.jonbake.report.exception.ReportRunningException;
import com.jonbake.report.exception.ReportsException;
import com.jonbake.report.util.ReportDatasource;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

/**
 * Service for running/obtaining information on all available reports.
 *
 * @author jonmbake
 */
public class ReportService {
    private final Map<String, JasperReport> reports;
    private final ReportDatasource reportDatasource;
    /**
     * Construct with {@link Configuration}.
     *
     * @param configuration - app configuration
     * @param reportDatasource - report data source
     */
    public ReportService (final Configuration configuration, final ReportDatasource reportDatasource) {
        this.reportDatasource = reportDatasource;
        String reportDirName = configuration.getReportsDirectory();
        if (reportDirName == null) {
            reports = Collections.EMPTY_MAP;
            return;
        }
        File reportDir = new File(reportDirName);
        if (!reportDir.isDirectory()) {
            throw new ConfigurationException(EnvironmentConfiguration.JASPER_REPORTS_DIR
                + " must be a valid directory.");
        }
        reports = Arrays.stream(reportDir.listFiles())
            .filter((f) -> "jrxml".equals(Files.getFileExtension(f.getName())))
            .collect(Collectors.toMap((file) -> Files.getNameWithoutExtension(file.getName()),
                    (file) -> {
                        try {
                            return JasperCompileManager.compileReport(file.getAbsolutePath());
                        } catch (JRException ex) {
                            Logger.getLogger(ReportService.class.getName()).log(Level.SEVERE, null, ex);
                            throw new ReportsException("Unexpected error when attempting to compile report file "
                                + file.getAbsolutePath(), "Report Compile Error");
                        }
                    }
                )
            );
    }
    /**
     * Returns a list of all available report names.
     *
     * @return - list of report names
     */
    public final Set<String> getReportList () {
        return this.reports.keySet();
    }
    /**
     * Run the report.  Will return {@link Optional#empty()} if report can't be found.
     *
     * @param reportName name of report to run
     * @param reportParams report parameters
     * @return Optional {@link JasperPrint}
     */
    public final Optional<JasperPrint> run (final String reportName, final Map<String, Object> reportParams) {
        JasperReport report = reports.get(reportName);
        if (report == null) {
            return Optional.empty();
        }
        Optional<Connection> dbConnection;
        try {
            dbConnection = reportDatasource.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(ReportService.class.getName()).log(Level.SEVERE, null, ex);
            throw new ReportRunningException("Unexpected error while attempting to get database connnection.");
        }
        try {
            if (!dbConnection.isPresent()) {
                return Optional.of(JasperFillManager.fillReport(report, reportParams, new JREmptyDataSource()));
            }
            return Optional.of(JasperFillManager.fillReport(report, reportParams, dbConnection.get()));
        } catch (JRException ex) {
            Logger.getLogger(ReportService.class.getName()).log(Level.SEVERE, null, ex);
            throw new ReportRunningException("Unexpected error while attempting to fill report " + reportName);
        }
    }
}
