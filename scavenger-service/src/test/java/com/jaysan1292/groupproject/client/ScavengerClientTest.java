package com.jaysan1292.groupproject.client;

import com.jaysan1292.groupproject.data.Player;
import com.jaysan1292.groupproject.data.PlayerBuilder;
import com.jaysan1292.groupproject.exceptions.GeneralServiceException;
import com.jaysan1292.groupproject.service.ScavengerService;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static com.jaysan1292.groupproject.Global.log;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 * Created with IntelliJ IDEA.
 * Date: 28/11/12
 * Time: 6:14 PM
 *
 * @author Jason Recillo
 */
@RunWith(Enclosed.class)
public class ScavengerClientTest {
    private static ScavengerClient client;

    private ScavengerClientTest() {}

    @BeforeClass
    public static void setUpService() throws Exception {
        log.info("Starting web service client tests.");
        ScavengerService.start(new String[]{"--local", "--debug"});

        client = new ScavengerClient();
    }

    @AfterClass
    public static void tearDownService() throws Exception {
        log.info("Web service client tests finished!");
        ScavengerService.stop();
    }

    public static class PlayerTests implements AccessorTest {
        @Test
        public void testGet() throws Exception {
            log.info("Test: Retrieve existing player from service");
            Player expected = new PlayerBuilder()
                    .setPlayerId(0)
                    .setFirstName("Jason")
                    .setLastName("Recillo")
                    .setStudentId("100123123")
                    .build();

            Player actual = client.getPlayer(0);

            assertEquals(expected, actual);
        }

        @Test
        public void testCreate() throws Exception {
            log.info("Test: Create new player and send to service");
            Player expected = new PlayerBuilder()
                    .setLastName("Choi")
                    .setFirstName("Sooyoung")
                    .setStudentId("100145665")
                    .build();

            Player actual = client.createPlayer(expected);

            expected.setId(actual.getId());

            assertEquals(expected, actual);
        }

        @Test
        public void testUpdate() throws Exception {
            log.info("Test: Update player in service");
            Player original = client.getPlayer(0);

            Player expected = new PlayerBuilder(original)
                    .setFirstName("JD")
                    .build();

            Player actual = client.updatePlayer(expected);

            assertNotSame(original, actual);
            assertEquals(expected, actual);

            //revert changes
            client.updatePlayer(original);
        }

        @Test
        public void testDelete() throws Exception {
            log.info("Test: Delete player from service");
            Player toDelete = new PlayerBuilder()
                    .setLastName("Seo")
                    .setFirstName("Joohyun")
                    .setStudentId("100489456")
                    .build();
            long id = client.createPlayer(toDelete).getId();

            Player player = client.getPlayer(id);

            client.deletePlayer(player);

            try {
                client.getPlayer(player.getId());
            } catch (GeneralServiceException e) {
                return;
            }

            throw new Exception("Delete player failed.");
        }
    }

    private static interface AccessorTest {
        public void testGet() throws Exception;

        public void testCreate() throws Exception;

        public void testUpdate() throws Exception;

        public void testDelete() throws Exception;
    }
}
