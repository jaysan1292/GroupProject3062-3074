package com.jaysan1292.groupproject.service.db;

import com.jaysan1292.groupproject.data.Challenge;
import com.jaysan1292.groupproject.data.ChallengeBuilder;
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
        DatabaseHelper.initDatabase();
    }

    @AfterClass
    public static void tearDownOnce() throws Exception {
        log.info("Database tests completed.");
        DatabaseHelper.cleanDatabase();
    }

    public static class ChallengeManagerTest {
        private final ChallengeManager manager = new ChallengeManager();

        @Test
        public void testGet() throws Exception {
            log.info("Test: Get single challenge");
            Challenge expected = new ChallengeBuilder()
                    .setChallengeId(0)
                    .setChallengeText("First go there and do this thing.")
                    .build();

            Challenge actual = manager.get(0);

            assertEquals(expected, actual);
        }

        @Test
        public void testUpdate() throws Exception {
            log.info("Test: Update a challenege");
            Challenge original = manager.get(0);

            Challenge expected = new ChallengeBuilder(original)
                    .setChallengeText("Edited challenge: First go there and do this thing.")
                    .build();

            manager.update(expected);

            Challenge actual = manager.get(0);

            assertNotSame(original, actual);
            assertEquals(expected, actual);

            // Other tests will depend on an unchanged databse so revert changes here :p
            manager.delete(actual);
            manager.create(original);
            assertEquals(original, manager.get(0));
        }
    }
}
