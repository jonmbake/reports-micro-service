package com.jonbake.report.configuration;

import java.util.Optional;

/**
 * All necessary configuration needed for this reports service.
 *
 * @author jonmbake
 */
public interface Configuration {
    /**
     * The reports directory where report definitions live.
     *
     * @return path to reports directory
     */
    String getReportsDirectory ();
    /**
     * JWT secret.  Needed for authentication.
     *
     * @return JWT secret
     */
    String getJWTSecret ();
    /**
     * JWT audience.  If provided will also validate audience during authentication.
     *
     * @return JWT audience if provided
     */
    Optional<String> getJWTAudience ();
    /**
     * JWT issuer.  If provided will also validate issuer during authentication.
     *
     * @return JWT issuer if provided
     */
    Optional<String> getJWTIssuer ();
    /**
     * JDBC Connection String.  For example: jdbc:mysql://localhost:3306/mydatabase?user=me&password=mypassword.  If
     * not set will use an empty data source.
     *
     * @return JDBC Connection String
     */
    Optional<String> getJDBCConnectionString ();
}
