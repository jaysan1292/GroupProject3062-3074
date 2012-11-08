package com.jaysan1292.groupproject.service;

import com.sun.grizzly.http.SelectorThread;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.header.MediaTypes;
import junit.framework.TestCase;

public class MainTest extends TestCase {

    private SelectorThread threadSelector;

    private WebResource r;

    public MainTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        threadSelector = Main.startServer();

        Client c = Client.create();
        r = c.resource(Main.BASE_URI);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        threadSelector.stopEndpoint();
    }

    /**
     * Test if a WADL document is available at the relative path
     * "application.wadl".
     */
    public void testApplicationWadl() {
        String serviceWadl = r.path("application.wadl").
                accept(MediaTypes.WADL).get(String.class);

        assertTrue(!serviceWadl.isEmpty());
    }
}
