package com.jaysan1292.groupproject.service.db;

import com.jaysan1292.groupproject.data.Player;
import com.jaysan1292.groupproject.data.PlayerBuilder;
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
 * Date: 27/11/12
 * Time: 5:19 PM
 *
 * @author Jason Recillo
 */
@RunWith(Enclosed.class)
public class DatabaseTest {
    private DatabaseTest() {}

    @BeforeClass
    public static void setUpOnce() throws Exception {
        log.info("Starting database tests.");
        try {
            DatabaseHelper.initDatabase();
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            System.exit(-1);
        }
    }

    @AfterClass
    public static void tearDownOnce() throws Exception {
        log.info("Database tests completed.");
        DatabaseHelper.cleanDatabase();
    }

    public static class PlayerManagerTest {
        private final PlayerManager manager = new PlayerManager();

        @Test
        public void testGet() throws Exception {
            log.info("Test: Get single Player");
            Player expected = new PlayerBuilder()
                    .setPlayerId(0)
                    .setFirstName("Jason")
                    .setLastName("Recillo")
                    .setStudentId("100726948")
                    .build();

            Player actual = manager.get(0);

            assertEquals(expected, actual);
        }

        @Test
        public void testUpdate() throws Exception {
            log.info("Test: Update a challenege");
            Player original = manager.get(0);

            Player expected = new PlayerBuilder(original)
                    .setFirstName("John")
                    .setLastName("Smith")
                    .build();

            manager.update(expected);

            Player actual = manager.get(0);

            assertNotSame(original, actual);
            assertEquals(expected, actual);

            // Other tests will depend on an unchanged databse so revert changes here :p
            manager.update(original);
        }

        @Test
        public void testCreate() throws Exception {
            log.info("Test: Create new Player");

            Player newPlayer = new PlayerBuilder()
                    .setFirstName("John")
                    .setLastName("Smith")
                    .setStudentId("123456789")
                    .build();

            manager.insert(newPlayer);

            throw new Exception("This test isn't done being written yet :3");
        }
    }
}
