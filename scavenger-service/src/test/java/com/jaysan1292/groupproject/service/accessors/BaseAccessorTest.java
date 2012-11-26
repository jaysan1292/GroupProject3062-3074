package com.jaysan1292.groupproject.service.accessors;

import com.jaysan1292.groupproject.Global;
import com.jaysan1292.groupproject.service.ScavengerService;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * Created with IntelliJ IDEA.
 * Date: 26/11/12
 * Time: 1:59 PM
 *
 * @author Jason Recillo
 */
public class BaseAccessorTest {
    protected static Client client;
    protected static WebResource resource;

    @BeforeClass
    public static void setUpOnce() throws Exception {
        Global.log.info("Starting tests.");
        ScavengerService.start(new String[]{"--local", "--debug"});

        client = Client.create();
        resource = client.resource("http://localhost:9000/service");
    }

    @AfterClass
    public static void tearDownOnce() throws Exception {
        Global.log.info("Tests finished!");
        ScavengerService.stop();
    }
}
