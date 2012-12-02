package com.jaysan1292.groupproject.service.accessors;

import com.jaysan1292.groupproject.data.*;
import com.jaysan1292.groupproject.service.ScavengerService;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

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
    private static final String URL_BASE = "http://localhost:9000/service";

    private WebServiceTest() {}

    @BeforeClass
    public static void setUpOnce() throws Exception {
        log.info("Starting web service tests.");
        ScavengerService.start(new String[]{"--local", "--debug"});

        client = Client.create();
        resource = client.resource(URL_BASE);
    }

    @AfterClass
    public static void tearDownOnce() throws Exception {
        log.info("Web service tests finished!");
        ScavengerService.stop();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    public static class AuthTest {
        @Test
        public void testAuthorizeAdmin() throws Exception {
            Player admin = new Player().readJSON(resource.path("players/5").get(String.class));
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    /** Player tests */
    public static class PlayerAccessorTest {
        private final WebResource playerRes = resource.path("players");

        @Test
        public void testGet() throws Exception {
            log.info("Test: Get specific player");
            Player expected = new PlayerBuilder()
                    .setPlayerId(0)
                    .setFirstName("Jason")
                    .setLastName("Recillo")
                    .setStudentId("100123123")
                    .build();

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
            assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());

            // Ensure the returned location is correct
            String location = response.getHeaders().getFirst("Location");
            assertEquals(URL_BASE + "/players/5", location);

            Player actual = new Player().readJSON(client.resource(location).get(String.class));
            expected.setId(actual.getId());

            assertEquals(expected, actual);
        }

        @Test
        public void testUpdate() throws Exception {
            log.info("Test: Update existing user");

            Player expected = new PlayerBuilder(new Player().<Player>readJSON(playerRes.path("0").get(String.class)))
                    .setFirstName("JD")
                    .build();

            MultivaluedMap<String, String> post = new MultivaluedMapImpl();
            post.putSingle("firstName", "JD");

            ClientResponse response = playerRes.path("0")
                                               .put(ClientResponse.class, post);

            // HTTP status 204: NO CONTENT
            assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());

            Player actual = new Player().readJSON(playerRes.path("0").get(String.class));

            assertEquals(expected, actual);
        }

        @Test
        public void testDelete() throws Exception {
            log.info("Test: Delete existing user");
            ClientResponse response = playerRes.path("5").delete(ClientResponse.class);

            assertEquals(Response.Status.ACCEPTED.getStatusCode(), response.getStatus());

            ClientResponse sanityCheck = playerRes.path("5").get(ClientResponse.class);
            assertEquals(Response.Status.NOT_FOUND.getStatusCode(), sanityCheck.getStatus());
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
            log.info("Test: Add new checkpoint");
            Checkpoint expected = new CheckpointBuilder()
                    .setLatitude(43.123456f)
                    .setLongitude(-79.123456f)
                    .setChallenge(new ChallengeBuilder()
                                          .setChallengeId(0)
                                          .setChallengeText("First go there and do this thing.")
                                          .build())
                    .build();

            ClientResponse response = checkpointRes.type(MediaType.APPLICATION_JSON_TYPE)
                                                   .post(ClientResponse.class, expected.toString());

            assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());

            String location = response.getHeaders().getFirst("Location");
            assertEquals(URL_BASE + "/checkpoints/3", location);

            Checkpoint actual = new Checkpoint().readJSON(client.resource(location).get(String.class));
            expected.setId(actual.getId());

            assertEquals(expected, actual);
        }

        @Test
        public void testUpdate() throws Exception {
            log.info("Test: Update existing checkpoint");
            Checkpoint expected = new CheckpointBuilder()
                    .setCheckpointId(0)
                    .setLatitude(43.675854f)
                    .setLongitude(-79.71069f)
                    .setChallenge(new ChallengeBuilder()
                                          .setChallengeId(1)
                                          .setChallengeText("Go here and there.")
                                          .build())
                    .build();

            MultivaluedMap<String, String> post = new MultivaluedMapImpl();
            post.putSingle("challenge", "1");

            ClientResponse response = checkpointRes.path("0")
                                                   .put(ClientResponse.class, post);

            assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());

            Checkpoint actual = new Checkpoint().readJSON(checkpointRes.path("0")
                                                                       .get(String.class));

            assertEquals(expected, actual);
        }

        @Test
        public void testDelete() throws Exception {
            log.info("Test: Delete existing checkpoint");
            ClientResponse response = checkpointRes.path("3").delete(ClientResponse.class);

            assertEquals(Response.Status.ACCEPTED.getStatusCode(), response.getStatus());

            ClientResponse sanityCheck = checkpointRes.path("3").get(ClientResponse.class);
            assertEquals(Response.Status.NOT_FOUND.getStatusCode(), sanityCheck.getStatus());
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    /** Challenge tests */
    public static class ChallengeAccessorTest {
        private final WebResource challengeRes = resource.path("challenges");

        @Test
        public void testGet() throws Exception {
            log.info("Test: Get specific challenge");
            Challenge expected = new ChallengeBuilder()
                    .setChallengeId(0)
                    .setChallengeText("First go there and do this thing.")
                    .build();

            Challenge actual = new Challenge().readJSON(challengeRes.path("0").get(String.class));

            assertEquals(expected, actual);
        }

        @Test
        public void testCreate() throws Exception {
            log.info("Test: Add new challenge");
            Challenge expected = new ChallengeBuilder()
                    .setChallengeText("Do something at this place over here.")
                    .build();

            ClientResponse response = challengeRes.type(MediaType.APPLICATION_JSON_TYPE)
                                                  .post(ClientResponse.class, expected.toString());

            assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());

            String location = response.getHeaders().getFirst("Location");
            assertEquals(URL_BASE + "/challenges/3", location);

            Challenge actual = new Challenge().readJSON(client.resource(location).get(String.class));
            expected.setId(actual.getId());

            assertEquals(expected, actual);
        }

        @Test
        public void testUpdate() throws Exception {
            log.info("Test: Update existing challenge");
            Challenge expected = new Challenge().readJSON(challengeRes.path("0").get(String.class));
            expected.setChallengeText(expected.getChallengeText() + " Also go do this.");

            MultivaluedMap<String, String> post = new MultivaluedMapImpl();
            post.putSingle("challengeText", expected.getChallengeText());

            ClientResponse response = challengeRes.path("0")
                                                  .put(ClientResponse.class, post);

            assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());

            Challenge actual = new Challenge().readJSON(challengeRes.path("0").get(String.class));

            assertEquals(expected, actual);
        }

        @Test
        public void testDelete() throws Exception {
            log.info("Test: Delete existing challenge");
            ClientResponse response = challengeRes.path("3").delete(ClientResponse.class);

            assertEquals(Response.Status.ACCEPTED.getStatusCode(), response.getStatus());

            ClientResponse sanityCheck = challengeRes.path("3").get(ClientResponse.class);
            assertEquals(Response.Status.NOT_FOUND.getStatusCode(), sanityCheck.getStatus());
        }
    }
}
