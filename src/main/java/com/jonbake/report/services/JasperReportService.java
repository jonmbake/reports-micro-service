package com.jonbake.report.services;

import com.google.common.io.Files;
import com.jonbake.report.exception.ConfigurationException;
import com.jonbake.report.configuration.Configuration;
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
 * Service for running/obtaining information on all available Jasper reports.
 *
 * @author jonmbake
 */
public class JasperReportService implements ReportService<JasperPrint> {
    private final Map<String, JasperReport> reports;
    private final ReportDatasource reportDatasource;
    /**
     * Construct with {@link Configuration}.
     *
     * @param configuration - app configuration
     * @param reportDatasource - report data source
     */
    public JasperReportService (final Configuration configuration, final ReportDatasource reportDatasource) {
        this.reportDatasource = reportDatasource;
        String reportDirName = configuration.getReportsDirectory();
        if (reportDirName == null) {
            reports = Collections.EMPTY_MAP;
            return;
        }
        File reportDir = new File(reportDirName);
        if (!reportDir.isDirectory()) {
            throw new ConfigurationException(reportDirName + " must be a valid directory accesible by Tomcat.");
        }
        reports = Arrays.stream(reportDir.listFiles())
            .filter((f) -> "jrxml".equals(Files.getFileExtension(f.getName())))
            .collect(Collectors.toMap((file) -> Files.getNameWithoutExtension(file.getName()),
                    (file) -> {
                        try {
                            return JasperCompileManager.compileReport(file.getAbsolutePath());
                        } catch (JRException ex) {
                            Logger.getLogger(JasperReportService.class.getName()).log(Level.SEVERE, null, ex);
                            throw new ReportsException("Unexpected error when attempting to compile report file "
                                + file.getAbsolutePath(), "Report Compile Error");
                        }
                    }
                )
            );
    }
    /**
     * {@inheritDoc}
     */
    public final Set<String> getReportList () {
        return this.reports.keySet();
    }
    /**
     * {@inheritDoc}
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
            Logger.getLogger(JasperReportService.class.getName()).log(Level.SEVERE, null, ex);
            throw new ReportRunningException("Unexpected error while attempting to get database connnection.");
        }
        try {
            if (!dbConnection.isPresent()) {
                return Optional.of(JasperFillManager.fillReport(report, reportParams, new JREmptyDataSource()));
            }
            return Optional.of(JasperFillManager.fillReport(report, reportParams, dbConnection.get()));
        } catch (JRException ex) {
            Logger.getLogger(JasperReportService.class.getName()).log(Level.SEVERE, null, ex);
            throw new ReportRunningException("Unexpected error while attempting to fill report " + reportName);
        }
    }
}
