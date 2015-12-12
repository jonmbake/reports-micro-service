package com.jonbake.report.rest;

import com.jonbake.report.exception.ReportRunningException;
import com.jonbake.report.util.ReportUtil;
import com.jonbake.report.services.ReportService;
import java.io.OutputStream;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

/**
 * REST resource for running reports.
 */
@Path("filled")
public class ReportRunnerResource {
    private final ReportService reportService;
    /**
     * Construct with {@link ReportService}.
     *
     * @param reportService - report service
     */
    public ReportRunnerResource (final ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * Run (fill) a report.
     *
     * @param reportName report name to run
     * @param uriInfo uri info needed to obtain query parameters
     * @return {@link Response} with PDF of report output
     */
    @GET
    @Produces("application/pdf")
    @Path("/{reportname}")
    public final Response run (@PathParam("reportname") final String reportName, @Context final UriInfo uriInfo) {
        final Optional<JasperPrint> filledReport = reportService.run(reportName, extractReportParams(uriInfo));

        if (!filledReport.isPresent()) {
            return Response.status(Status.NOT_FOUND).build();
        }

        return Response.ok((StreamingOutput) (OutputStream output) -> {
            try {
                JasperExportManager.exportReportToPdfStream(filledReport.get(), output);
            } catch (JRException ex) {
                Logger.getLogger(ReportRunnerResource.class.getName()).log(Level.SEVERE, null, ex);
                throw new ReportRunningException("Unexpected error while attempting to generate PDF for "
                    + reportName + " report.");
            }
        }).build();
    }
    /**
     * Extract report params from URL query parameters.  Will also convert report params to valid java types.
     *
     * @param uriInfo @see {@link UriInfo}
     * @return jasper report params
     */
    private Map<String, Object> extractReportParams (final UriInfo uriInfo) {
        return uriInfo.getQueryParameters().entrySet().stream()
            .filter((p) -> p.getKey().toLowerCase().equals("jwt"))
            .collect(Collectors.toMap((p) -> p.getKey().toLowerCase(), ReportUtil.QUERY_TO_REPORT_PARAM));
    }
}
