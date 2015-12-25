package com.jonbake.report.filters;

import com.auth0.jwt.JWTVerifier;
import com.jonbake.report.configuration.Configuration;
import com.jonbake.report.configuration.EnvironmentConfiguration;
import com.jonbake.report.exception.ConfigurationException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.apache.commons.codec.binary.Base64;
/**
 * Authenticate every request to report resources using JWT token passed as query parameter.
 *
 * @author jonmbake
 */
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthFilter implements ContainerRequestFilter {
    public static final Base64 DECODER = new Base64();;
    private final Configuration configuration;
    /**
     * Construct with app configuration.
     *
     * @param configuration - app configuration
     */
    public AuthFilter (final Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public final void filter (final ContainerRequestContext crc) throws IOException {
        String token = crc.getUriInfo().getQueryParameters().getFirst("jwt");
        if (token == null) {
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }
        String jwtSecret = configuration.getJWTSecret();
        if (jwtSecret == null) {
            throw new ConfigurationException("Must set the " + EnvironmentConfiguration.JWT_SECRET
                + " environment variable.");
        }
        byte[] secret = DECODER.decodeBase64(jwtSecret);
        //Map<String,Object> payload;
        try {
            new JWTVerifier(
                    secret,
                    configuration.getJWTAudience().orElse(null),
                    configuration.getJWTIssuer().orElse(null)
            ).verify(token);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "JWT verification failed", ex);
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }
    }
}
