package com.jonbake.report;

import com.jonbake.report.exception.mappers.ReportsExceptionMapper;
import com.jonbake.report.filters.AuthFilter;
import com.jonbake.report.rest.ReportRunnerResource;
import com.jonbake.report.configuration.Configuration;
import com.jonbake.report.configuration.EnvironmentConfiguration;
import com.jonbake.report.rest.ReportInfoResource;
import com.jonbake.report.services.JasperReportService;
import com.jonbake.report.util.ReportDatasource;
import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Register components.
 *
 * @author jonmbake
 */
@ApplicationPath("/reports")
public class App extends ResourceConfig {
    /**
     * Construct with {@link EnvironmentConfiguration}.
     */
    public App () {
        this(new EnvironmentConfiguration());
    }
    /**
     * Construct app with non-environment config.  This is useful for tests.
     *
     * @param configuration - @see {@link Configuration}
     */
    public App (final Configuration configuration) {
        register(ReportsExceptionMapper.class);
        register(LoggingFilter.class);
        JasperReportService rs = new JasperReportService(configuration, new ReportDatasource(configuration));
        register(new ReportRunnerResource(rs));
        register(new ReportInfoResource(rs));
        register(new AuthFilter(configuration));
    }
}
