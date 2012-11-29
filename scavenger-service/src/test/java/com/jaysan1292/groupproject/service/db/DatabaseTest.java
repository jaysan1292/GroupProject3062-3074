package com.jaysan1292.groupproject.service.db;

import com.google.common.collect.Lists;
import com.jaysan1292.groupproject.data.*;
import com.jaysan1292.groupproject.exceptions.GeneralServiceException;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
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
            log.info("-------------------------------------");
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
        @Override
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
        @Override
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
        @Override
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
        @Override
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
        @Override
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
        @Override
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
        @Override
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
        @Override
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

    public static class PathManagerTest implements ManagerTest {
        private final PathManager manager = new PathManager();
        private final ArrayList<Checkpoint> checkpoints = Lists.newArrayList(
                new CheckpointBuilder()
                        .setCheckpointId(0)
                        .setLatitude(43.675854f)
                        .setLongitude(-79.71069f)
                        .setChallenge(new ChallengeBuilder()
                                              .setChallengeId(0)
                                              .setChallengeText("First go there and do this thing.")
                                              .build())
                        .build(),
                new CheckpointBuilder()
                        .setCheckpointId(1)
                        .setLatitude(43.676130f)
                        .setLongitude(-79.410492f)
                        .setChallenge(new ChallengeBuilder()
                                              .setChallengeId(1)
                                              .setChallengeText("Go here and there.")
                                              .build())
                        .build(),
                new CheckpointBuilder()
                        .setCheckpointId(2)
                        .setLatitude(43.6754555f)
                        .setLongitude(-79.410492f)
                        .setChallenge(new ChallengeBuilder()
                                              .setChallengeId(2)
                                              .setChallengeText("Do this thing at this place.")
                                              .build())
                        .build());

        @Test
        @Override
        public void testGet() throws Exception {
            log.info("Test: Get path from database");
            Path expected = new PathBuilder()
                    .setPathId(0)
                    .setCheckpoints(new ArrayList<Checkpoint>(2) {{
                        add(checkpoints.get(0));
                        add(checkpoints.get(1));
                    }})
                    .build();

            Path actual = manager.get(0);

            assertEquals(expected, actual);
        }

        @Test
        @Override
        public void testUpdate() throws Exception {
            log.info("Test: Update a path");
            final Path original = manager.get(0);

            Path expected = new PathBuilder(original)
                    .setCheckpoints(new ArrayList<Checkpoint>() {{
                        for (Checkpoint checkpoint : original.getCheckpoints()) {
                            add(checkpoint);
                        }
                        add(checkpoints.get(2));
                    }})
                    .build();

            manager.update(expected);

            Path actual = manager.get(0);

            assertNotSame(original, actual);
            assertEquals(expected, actual);

            // Other tests will depend on an unchanged databse so revert changes here :p
            manager.update(original);
        }

        @Test
        @Override
        public void testInsert() throws Exception {
            log.info("Test: Create new path");

            Path expected = new PathBuilder()
                    .setCheckpoints(new ArrayList<Checkpoint>() {{
                        add(checkpoints.get(1));
                        add(checkpoints.get(2));
                    }})
                    .build();

            long id = manager.insert(expected);

            Path actual = manager.get(id);

            expected.setId(id);

            assertEquals(expected, actual);
        }

        @Test
        @Override
        public void testDelete() throws Exception {
            log.info("Test: Delete existing path");

            // Create a new item for use in this test
            Path toDelete = new PathBuilder()
                    .setCheckpoints(new ArrayList<Checkpoint>(2) {{
                        add(checkpoints.get(0));
                        add(checkpoints.get(1));
                        add(checkpoints.get(2));
                    }})
                    .build();
            long id = manager.insert(toDelete);

            Path path = manager.get(id);

            manager.delete(path);

            try {
                manager.get(path.getId());
            } catch (GeneralServiceException ignored) {
                // If that statement throws an exception; the item was not found in the database,
                // which is totally expected and what we want. So, this test passed.
                return;
            }

            throw new Exception("Delete team failed.");
        }
    }

////////////////////////////////////////////////////////////////////////////////////////////////////

    public static class CheckpointManagerTest implements ManagerTest {
        private final CheckpointManager manager = new CheckpointManager();
        private final ArrayList<Challenge> challenges = Lists.newArrayList(
                new ChallengeBuilder()
                        .setChallengeId(0)
                        .setChallengeText("First go there and do this thing.")
                        .build(),
                new ChallengeBuilder()
                        .setChallengeId(1)
                        .setChallengeText("Go here and there.")
                        .build(),
                new ChallengeBuilder()
                        .setChallengeId(2)
                        .setChallengeText("Do this thing at this place.")
                        .build());

        @Test
        @Override
        public void testGet() throws Exception {
            log.info("Test: Get checkpoint from database");
            Checkpoint expected = new CheckpointBuilder()
                    .setCheckpointId(0)
                    .setLatitude(43.675854f)
                    .setLongitude(-79.71069f)
                    .setChallenge(challenges.get(0))
                    .build();

            Checkpoint actual = manager.get(0);

            assertEquals(expected, actual);
        }

        @Test
        @Override
        public void testUpdate() throws Exception {
            log.info("Test: Update a checkpoint");
            Checkpoint original = manager.get(0);

            Checkpoint expected = new CheckpointBuilder(original)
                    .setLongitude(42.424242f)
                    .setLatitude(-79.123456f)
                    .build();

            manager.update(expected);

            Checkpoint actual = manager.get(0);

            assertNotSame(original, actual);
            assertEquals(expected, actual);

            // Other tests will depend on an unchanged databse so revert changes here :p
            manager.update(original);
        }

        @Test
        @Override
        public void testInsert() throws Exception {
            log.info("Test: Create new checkpoint");

            Checkpoint expected = new CheckpointBuilder()
                    .setLatitude(42.123456f)
                    .setLongitude(-79.192168f)
                    .setChallenge(challenges.get(0))
                    .build();

            long id = manager.insert(expected);

            Checkpoint actual = manager.get(id);

            expected.setId(id);

            assertEquals(expected, actual);
        }

        @Test
        @Override
        public void testDelete() throws Exception {
            log.info("Test: Delete existing checkpoint");

            // Create a new item for use in this test
            Checkpoint toDelete = new CheckpointBuilder()
                    .setLatitude(43.197545f)
                    .setLongitude(-79.456454f)
                    .setChallenge(challenges.get(2))
                    .build();
            long id = manager.insert(toDelete);

            Checkpoint checkpoint = manager.get(id);

            manager.delete(checkpoint);

            try {
                manager.get(checkpoint.getId());
            } catch (GeneralServiceException ignored) {
                // If that statement throws an exception; the item was not found in the database,
                // which is totally expected and what we want. So, this test passed.
                return;
            }

            throw new Exception("Delete team failed.");
        }
    }

////////////////////////////////////////////////////////////////////////////////////////////////////

    public static class ChallengeManagerTest implements ManagerTest {
        private final ChallengeManager manager = new ChallengeManager();

        @Test
        @Override
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
        @Override
        public void testUpdate() throws Exception {
            log.info("Test: Update a challenge");
            Challenge original = manager.get(0);

            Challenge expected = new ChallengeBuilder(original)
                    .setChallengeText("First go there and do this thing, and then you have to go somewhere completely different.")
                    .build();

            manager.update(expected);

            Challenge actual = manager.get(0);

            assertNotSame(original, actual);
            assertEquals(expected, actual);

            // Other tests will depend on an unchanged databse so revert changes here :p
            manager.update(original);
        }

        @Test
        @Override
        public void testInsert() throws Exception {
            log.info("Test: Create new challenge");

            Challenge expected = new ChallengeBuilder()
                    .setChallengeText("Do something here.")
                    .build();

            long id = manager.insert(expected);

            Challenge actual = manager.get(id);

            expected.setId(id);

            assertEquals(expected, actual);
        }

        @Test
        @Override
        public void testDelete() throws Exception {
            log.info("Test: Delete existing challenge");

            // Create a new challenge for this test
            Challenge toDelete = new ChallengeBuilder()
                    .setChallengeText("Do something at this location.")
                    .build();
            long id = manager.insert(toDelete);

            Challenge challenge = manager.get(id);

            manager.delete(challenge);

            try {
                manager.get(challenge.getId());
            } catch (GeneralServiceException ignored) {
                // If that statement throws an exception; the item was not found in the database,
                // which is totally expected and what we want. So, this test passed.
                return;
            }

            throw new Exception("Delete challenge failed.");
        }
    }

////////////////////////////////////////////////////////////////////////////////////////////////////

    public static class ScavengerHuntManagerTest implements ManagerTest {
        private final ScavengerHuntManager manager = new ScavengerHuntManager();
        private final PathManager paths = new PathManager();
        private final TeamManager teams = new TeamManager();

        @Test
        @Override
        public void testGet() throws Exception {
            log.info("Test: Get single scavenger hunt");
            ScavengerHunt expected = new ScavengerHuntBuilder()
                    .setScavengerHuntId(0)
                    .setPath(paths.get(0))
                    .setTeam(teams.get(0))
                    .setStartTime(new DateTime(2012, 11, 27, 10, 0,
                                               DateTimeZone.getDefault()))
                    .setFinishTime(new DateTime(2012, 11, 27, 16, 0,
                                                DateTimeZone.getDefault()))
                    .build();

            ScavengerHunt actual = manager.get(0);

            assertEquals(expected, actual);
        }

        @Test
        @Override
        public void testUpdate() throws Exception {
            log.info("Test: Update a scavenger hunt");
            ScavengerHunt original = manager.get(0);

            ScavengerHunt expected = new ScavengerHuntBuilder(original)
                    .setFinishTime(new DateTime(2012, 11, 27, 20, 0,
                                                DateTimeZone.getDefault()))
                    .build();

            manager.update(expected);

            ScavengerHunt actual = manager.get(0);

            assertNotSame(original, actual);
            assertEquals(expected, actual);

            // Other tests will depend on an unchanged databse so revert changes here :p
            manager.update(original);
        }

        @Test
        @Override
        public void testInsert() throws Exception {
            log.info("Test: Create new scavenger hunt");

            ScavengerHunt expected = new ScavengerHuntBuilder()
                    .setTeam(teams.get(1))
                    .setPath(paths.get(0))
                    .setStartTime(new DateTime(2012, 11, 30, 11, 0,
                                               DateTimeZone.getDefault()))
                    .setFinishTime(new DateTime(2012, 11, 30, 16, 0,
                                                DateTimeZone.getDefault()))
                    .build();

            long id = manager.insert(expected);

            ScavengerHunt actual = manager.get(id);

            expected.setId(id);

            assertEquals(expected, actual);
        }

        @Test
        @Override
        public void testDelete() throws Exception {
            log.info("Test: Delete existing scavenger hunt");

            // Create a new scavenger hunt for this test
            ScavengerHunt toDelete = new ScavengerHuntBuilder()
                    .setTeam(teams.get(1))
                    .setPath(paths.get(0))
                    .setStartTime(new DateTime(2012, 11, 30, 11, 0,
                                               DateTimeZone.getDefault()))
                    .setFinishTime(new DateTime(2012, 11, 30, 16, 0,
                                                DateTimeZone.getDefault()))
                    .build();
            long id = manager.insert(toDelete);

            ScavengerHunt scavengerhunt = manager.get(id);

            manager.delete(scavengerhunt);

            try {
                manager.get(scavengerhunt.getId());
            } catch (GeneralServiceException ignored) {
                // If that statement throws an exception; the item was not found in the database,
                // which is totally expected and what we want. So, this test passed.
                return;
            }

            throw new Exception("Delete scavenger hunt failed.");
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
