package com.jonbake.report.exception.mappers;

import com.google.common.collect.ImmutableMap;
import com.jonbake.report.exception.ReportsException;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Maps {@link ReportsException} and all super classes to JSON response with user friendly error message.
 *
 * @author jonmbake
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
public class ReportsExceptionMapper implements ExceptionMapper<ReportsException> {
    @Override
    public final Response toResponse (final ReportsException e) {
        return Response.status(Response.Status.BAD_REQUEST)
            .entity(ImmutableMap.of("errorMessage", e.getMessage(), "type", e.getType()))
            .type(MediaType.APPLICATION_JSON)
            .build();
    }
}
