package com.jonbake.report.rest;


import com.jonbake.report.App;
import com.jonbake.report.configuration.TestConfiguration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.test.JerseyTest;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Integration tests for {@link ReportInfoResource}.
 *
 * @author jonmbake
 */
public class ReportInfoResourceTest extends JerseyTest {
    private TestConfiguration testConfiguration;

    @Override
    protected Application configure() {    
        this.testConfiguration = new TestConfiguration();
        return new App(this.testConfiguration);
    }

    @Test
    public void testReportsInfo () {
        Response r = target("info").queryParam("jwt", this.testConfiguration.getTestUserToken()).request(MediaType.APPLICATION_JSON).get();
        assertEquals(Response.Status.OK.getStatusCode(), r.getStatus());
        Map m = r.readEntity(HashMap.class);
        assertTrue("Has only names property", m.size() == 1 && m.get("names") instanceof java.util.List);
        List<String> names = ((List<String>)m.get("names"));
        assertTrue("The report list only contained the test report",  names.size() == 1 && "TestJasper".equals(names.get(0)));
    }
}
