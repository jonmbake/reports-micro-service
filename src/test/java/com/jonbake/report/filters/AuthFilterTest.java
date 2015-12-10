/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonbake.report.filters;

import com.jonbake.report.App;
import com.jonbake.report.configuration.TestConfiguration;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.test.JerseyTest;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author jonmbake
 */
public class AuthFilterTest extends JerseyTest {
    private TestConfiguration testConfiguration;

    @Override
    protected Application configure() {    
        this.testConfiguration = new TestConfiguration();
        return new App(this.testConfiguration);
    }

    @Test
    public void canNotAccessResourcesWithoutToken () {
        Response r = target("info").request(MediaType.APPLICATION_JSON).get();
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), r.getStatus());
    }

    @Test
    public void canNotAccessResourcesWithInvalidToken () {
        Response r = target("info").queryParam("jwt", "NOT_A_VALID_TOKEN_182JKSDFASDGSDGsdg").request(MediaType.APPLICATION_JSON).get();
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), r.getStatus());
    }

    @Test
    public void canAccessResourcesWithValidToken () {
        Response r = target("info").queryParam("jwt", this.testConfiguration.getTestUserToken()).request(MediaType.APPLICATION_JSON).get();
        assertEquals(Response.Status.OK.getStatusCode(), r.getStatus());
    }
}
