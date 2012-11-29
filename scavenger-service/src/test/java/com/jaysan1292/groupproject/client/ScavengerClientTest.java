package com.jaysan1292.groupproject.client;

import com.jaysan1292.groupproject.data.Player;
import com.jaysan1292.groupproject.data.PlayerBuilder;
import com.jaysan1292.groupproject.service.ScavengerService;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static com.jaysan1292.groupproject.Global.log;
import static org.junit.Assert.assertEquals;

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

    @BeforeClass
    public static void setUpService() throws Exception {
        log.info("Starting tests.");
        ScavengerService.start(new String[]{"--local", "--debug"});

        client = new ScavengerClient();
    }

    @AfterClass
    public static void tearDownService() throws Exception {
        log.info("Tests finished!");
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

            Player actual = client.get(0);

            assertEquals(expected, actual);
        }

        @Test
        public void testCreate() throws Exception {
            //TODO: Auto-generated method stub
        }

        @Test
        public void testUpdate() throws Exception {
            //TODO: Auto-generated method stub
        }

        @Test
        public void testDelete() throws Exception {
            //TODO: Auto-generated method stub
        }
    }

    private static interface AccessorTest {
        public void testGet() throws Exception;

        public void testCreate() throws Exception;

        public void testUpdate() throws Exception;

        public void testDelete() throws Exception;
    }
}
