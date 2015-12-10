package com.jonbake.report.rest;

import com.google.common.collect.ImmutableMap;
import com.jonbake.report.services.ReportService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST resource to get metadata/info about reports.
 *
 * @author jonmbake
 */
@Path("info")
public class ReportInfoResource {
    private final ReportService reportService;
    /**
     * Construct with {@link ReportService}.
     *
     * @param reportService - report service
     */
    public ReportInfoResource (final ReportService reportService) {
        this.reportService = reportService;
    }
    /**
     * List of reports that the user has access to.
     *
     * @return List of reports
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public final Response getReports () {
        return Response.ok().entity(ImmutableMap.of("names", reportService.getReportList())).build();
    }
}
