package com.jaysan1292.groupproject.service.db;

import com.jaysan1292.groupproject.data.Player;
import com.jaysan1292.groupproject.data.PlayerBuilder;
import com.jaysan1292.groupproject.data.Team;
import com.jaysan1292.groupproject.data.TeamBuilder;
import com.jaysan1292.groupproject.exceptions.GeneralServiceException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.HashMap;

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
@FixMethodOrder(MethodSorters.DEFAULT)
public class DatabaseTest {
    private DatabaseTest() {}

    //region Test setup

    @BeforeClass
    public static void setUpOnce() throws Exception {
        log.info("Starting database tests.");
        try {
            DatabaseHelper.initDatabase();
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
            System.exit(-1);
        }
    }

    @AfterClass
    public static void tearDownOnce() throws Exception {
        log.info("Database tests completed.");
        DatabaseHelper.cleanDatabase();
    }

    //endregion

////////////////////////////////////////////////////////////////////////////////////////////////////

    public static class PlayerManagerTest implements ManagerTest {
        private final PlayerManager manager = new PlayerManager();

        @Test
        public void testGet() throws Exception {
            log.info("Test: Get single player");
            Player expected = new PlayerBuilder()
                    .setPlayerId(0)
                    .setFirstName("Jason")
                    .setLastName("Recillo")
                    .setStudentId("100123123")
                    .build();

            Player actual = manager.get(0);

            assertEquals(expected, actual);
        }

        @Test
        public void testUpdate() throws Exception {
            log.info("Test: Update a player");
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
        public void testInsert() throws Exception {
            log.info("Test: Create new player");

            Player expected = new PlayerBuilder()
                    .setFirstName("John")
                    .setLastName("Smith")
                    .setStudentId("123456789")
                    .build();

            long id = manager.insert(expected);

            Player actual = manager.get(id);

            // When instantiated without an id, its value will be -1. Here, we just set the id to
            // the id of the newly inserted item so that if the assertEquals() fails, then at least
            // it won't be the id's fault :p
            expected.setId(id);

            assertEquals(expected, actual);
        }

        @Test
        public void testDelete() throws Exception {
            log.info("Test: Delete existing player");

            // Create a new player for this test
            Player toDelete = new PlayerBuilder()
                    .setFirstName("Soonkyu")
                    .setLastName("Lee")
                    .setStudentId("100457985")
                    .build();
            long id = manager.insert(toDelete);

            Player player = manager.get(id);

            manager.delete(player);

            try {
                manager.get(player.getId());
            } catch (GeneralServiceException ignored) {
                // If that statement throws an exception; the item was not found in the database,
                // which is totally expected and what we want. So, this test passed.
                return;
            }

            throw new Exception("Delete player failed.");
        }
    }

////////////////////////////////////////////////////////////////////////////////////////////////////

    public static class TeamManagerTest implements ManagerTest {
        private final TeamManager manager = new TeamManager();

        @Test
        public void testGet() throws Exception {
            log.info("Test: Get single team");
            Team expected = new TeamBuilder()
                    .setTeamId(0)
                    .setTeamMembers(new HashMap<Long, Player>(2) {{
                        put(1L, new PlayerBuilder()
                                .setPlayerId(1)
                                .setFirstName("Peter")
                                .setLastName("Le")
                                .setStudentId("100145965")
                                .build());
                        put(2L, new PlayerBuilder()
                                .setPlayerId(2)
                                .setFirstName("Mellicent")
                                .setLastName("Dres")
                                .setStudentId("100793317")
                                .build());
                    }})
                    .build();

            Team actual = manager.get(0);

            assertEquals(expected, actual);
        }

        @Test
        public void testUpdate() throws Exception {
            log.info("Test: Update existing team");
            final Team original = manager.get(0);

            Team expected = new TeamBuilder(original)
                    .setTeamMembers(new HashMap<Long, Player>(2) {{
                        put(1L, original.getPlayer(1));
                        put(0L, new PlayerBuilder()
                                .setPlayerId(0)
                                .setFirstName("Jason")
                                .setLastName("Recillo")
                                .setStudentId("100123123")
                                .build());
                    }})
                    .build();

            manager.update(expected);

            Team actual = manager.get(0);

            assertNotSame(original, actual);
            assertEquals(expected, actual);

            // Revert changes for other tests
            manager.update(original);
        }

        @Test
        public void testInsert() throws Exception {
            log.info("Test: Create new team");

            // Create new players for the new team
            final PlayerManager p = new PlayerManager();
            final long player1 = p.insert(
                    new PlayerBuilder()
                            .setFirstName("Taeyeon")
                            .setLastName("Kim")
                            .setStudentId("100156789")
                            .build());
            final long player2 = p.insert(
                    new PlayerBuilder()
                            .setFirstName("Tiffany")
                            .setLastName("Hwang")
                            .setStudentId("100456785")
                            .build());

            Team expected = new TeamBuilder()
                    .setTeamMembers(new HashMap<Long, Player>(2) {{
                        put(player1, p.get(player1));
                        put(player2, p.get(player2));
                    }})
                    .build();

            long newid = manager.insert(expected);

            Team actual = manager.get(newid);

            expected.setId(newid);

            assertEquals(expected, actual);
        }

        @Test
        public void testDelete() throws Exception {
            log.info("Test: Delete existing team");

            // Create a new team for use in this test
            Team toDelete = new TeamBuilder()
                    .setTeamMembers(new HashMap<Long, Player>(1) {{
                        put(1L, new PlayerManager().get(1));
                    }})
                    .build();
            long id = manager.insert(toDelete);

            Team team = manager.get(id);

            manager.delete(team);

            try {
                manager.get(team.getId());
            } catch (GeneralServiceException ignored) {
                // If that statement throws an exception; the item was not found in the database,
                // which is totally expected and what we want. So, this test passed.
                return;
            }

            throw new Exception("Delete team failed.");
        }
    }

////////////////////////////////////////////////////////////////////////////////////////////////////

    private interface ManagerTest {
        public void testGet() throws Exception;

        public void testUpdate() throws Exception;

        public void testInsert() throws Exception;

        public void testDelete() throws Exception;
    }
}
