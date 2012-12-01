package com.jaysan1292.groupproject.service.accessors;

import com.jaysan1292.groupproject.data.*;
import com.jaysan1292.groupproject.service.ScavengerService;
import com.jaysan1292.groupproject.service.db.PlayerManager;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import javax.ws.rs.core.MediaType;

import static com.jaysan1292.groupproject.Global.log;
import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * Date: 26/11/12
 * Time: 1:59 PM
 *
 * @author Jason Recillo
 */
@RunWith(Enclosed.class)
public class WebServiceTest {
    protected static Client client;
    protected static WebResource resource;

    private WebServiceTest() {}

    @BeforeClass
    public static void setUpOnce() throws Exception {
        log.info("Starting web service tests.");
        ScavengerService.start(new String[]{"--local", "--debug"});

        client = Client.create();
        resource = client.resource("http://localhost:9000/service");
    }

    @AfterClass
    public static void tearDownOnce() throws Exception {
        log.info("Web service tests finished!");
        ScavengerService.stop();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    /** Player tests */
    public static class PlayerAccessorTest {
        private final WebResource playerRes = resource.path("players");
        private final PlayerManager manager = new PlayerManager();

        @Test
        public void testGet() throws Exception {
            log.info("Test: Get specific player");
            Player expected = manager.get(0);

            Player actual = new Player().readJSON(playerRes.path("0").get(String.class));

            assertEquals(expected, actual);
        }

        @Test
        public void testCreate() throws Exception {
            log.info("Test: Add new player");
            Player expected = new PlayerBuilder()
                    .setLastName("Kim")
                    .setFirstName("Taeyeon")
                    .setStudentId("100548956")
                    .build();

            ClientResponse response = playerRes.type(MediaType.APPLICATION_JSON_TYPE)
                                               .post(ClientResponse.class, expected.toString());

            // HTTP status 201: CREATED
            if (response.getStatus() != 201) {
                throw new RuntimeException("Failed: HTTP code " + response.getStatus() + " returned, expected 201");
            }

            Player actual = new Player().readJSON(response.getEntity(String.class));

            expected.setId(actual.getId());

            assertEquals(expected, actual);
        }

        @Test
        public void testUpdate() throws Exception {

        }

        @Test
        public void testDelete() throws Exception {

        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    /** Checkpoint tests */
    public static class CheckpointAccessorTest {
        private final WebResource checkpointRes = resource.path("checkpoints");

        @Test
        public void testGet() throws Exception {
            log.info("Test: Get specific checkpoint");
            Checkpoint expected = new CheckpointBuilder()
                    .setCheckpointId(0)
                    .setLatitude(43.675854f)
                    .setLongitude(-79.71069f)
                    .setChallenge(new ChallengeBuilder()
                                          .setChallengeId(0)
                                          .setChallengeText("First go there and do this thing.")
                                          .build())
                    .build();

            Checkpoint actual = new Checkpoint().readJSON(checkpointRes.path("0").get(String.class));

            assertEquals(expected, actual);
        }

        @Test
        public void testCreate() throws Exception {

        }

        @Test
        public void testUpdate() throws Exception {

        }

        @Test
        public void testDelete() throws Exception {

        }
    }
}
