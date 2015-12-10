package com.jonbake.report.configuration;

import com.jonbake.report.exception.ConfigurationException;
import java.util.Optional;
import org.apache.commons.lang3.ObjectUtils;

/**
 * Configurations set as Java environment properties.
 *
 * @author jonmbake
 */
public class EnvironmentConfiguration implements Configuration {
    public static final String JASPER_REPORTS_DIR = "JASPER_REPORTS_DIR";
    public static final String JWT_SECRET = "JWT_SECRET";
    public static final String JWT_AUDIENCE = "JWT_AUDIENCE";
    public static final String JWT_ISSUER = "JWT_ISSUER";
    public static final String JDBC_CONNECTION_STRING = "JDBC_CONNECTION_STRING";
    /**
     * {@inheritDoc}
     */
    @Override
    public final String getReportsDirectory () {
        return getlValue(JASPER_REPORTS_DIR);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public final String getJWTSecret () {
        return getlValue(JWT_SECRET);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public final Optional<String> getJWTAudience () {
        return getOptionalValue(JWT_AUDIENCE);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public final Optional<String> getJWTIssuer () {
        return getOptionalValue(JWT_ISSUER);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public final Optional<String> getJDBCConnectionString () {
        return getOptionalValue(JDBC_CONNECTION_STRING);
    }
    /**
     * Return an optional config value.  Checks both System and Environment properties.
     *
     * @param propertyName property name
     * @return configuration value if set
     */
    private Optional<String> getOptionalValue (final String propertyName) {
        return Optional.of(ObjectUtils.firstNonNull(System.getProperty(propertyName), System.getenv(propertyName)));
    }
    /**
     * Return an optional config value.  Checks both System and Environment properties.
     *
     * @param propertyName property name
     * @return configuration value if set
     */
    private String getlValue (final String propertyName) {
         String value = ObjectUtils.firstNonNull(System.getProperty(propertyName), System.getenv(propertyName));
         if (value == null) {
             throw new ConfigurationException(propertyName + " envrionment variable must be set.");
         }
         return value;
    }
}
