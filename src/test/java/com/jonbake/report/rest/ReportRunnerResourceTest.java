package com.jonbake.report.rest;

import com.jonbake.report.App;
import com.jonbake.report.configuration.TestConfiguration;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.test.JerseyTest;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author jonmbake
 */
public class ReportRunnerResourceTest extends JerseyTest {
    private TestConfiguration testConfiguration;

    @Override
    protected Application configure() {    
        this.testConfiguration = new TestConfiguration();
        return new App(this.testConfiguration);
    }
    
    @Test
    public void testNotFoundWhenRunningNonExisting () {
        Response r = target("filled").path("NotAReport").queryParam("jwt", this.testConfiguration.getTestUserToken()).request("application/pdf").get();
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), r.getStatus());
    }

    @Test
    public void testReportRunsSuccessfully () {
        Response r = target("filled").path("TestJasper").queryParam("jwt", this.testConfiguration.getTestUserToken()).request("application/pdf").get();
        assertEquals(Response.Status.OK.getStatusCode(), r.getStatus());
        //the response is a PDF which can't be parsed, just verify there is some content in the body
        byte[] body = r.readEntity(byte[].class);
        assertTrue("There is some body to the report running response", body.length > 2000);
    }
}
