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
import java.util.List;

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

            // Other tests will depend on an unchanged database so revert changes here :p
            manager.update(original);
        }

        @Test
        public void testInsert() throws Exception {
            log.info("Test: Create new player");

            Player expected = new PlayerBuilder()
                    .setLastName("Jung")
                    .setFirstName("Sooyeon")
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

        @Test(expected = GeneralServiceException.class)
        public void testGetNonExisting() throws Exception {
            log.info("Test: Get non-existent player");
            manager.get(100);
        }

        @Test(expected = IllegalArgumentException.class)
        public void testUpdateBadData() throws Exception {
            log.info("Test: Update player with invalid data");
            Player player = manager.get(0);
            player.setStudentNumber("1"); //student number must be 9 digits

            manager.update(player);
        }

        @Test(expected = IllegalArgumentException.class)
        public void testInsertBadData() throws Exception {
            log.info("Test: Insert player with invalid data");
            Player player = new PlayerBuilder()
                    .setLastName("Gong")
                    .setFirstName("Minji")
                    .setStudentId("123") //invalid
                    .build();

            manager.insert(player);
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
                    .setTeamMembers(new HashMap<Long, Player>(2) {{
                        put(1L, new PlayerManager().get(1));
                        put(2L, new PlayerManager().get(2));
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

        @Test(expected = GeneralServiceException.class)
        public void testGetNonExisting() throws Exception {
            log.info("Test: Get non existing team");
            manager.get(100);
        }

        @Test(expected = IllegalArgumentException.class)
        public void testUpdateBadData() throws Exception {
            log.info("Test: Update team with bad data");
            Team team = manager.get(0);
            // Teams must have at least two members
            team.setTeamMembers(new HashMap<Long, Player>() {{
                new Player();
            }});

            manager.update(team);
        }

        @Test(expected = IllegalArgumentException.class)
        public void testInsertBadData() throws Exception {
            log.info("Test: Insert team with bad data");
            Team team = new TeamBuilder()
                    .setTeamMembers(new HashMap<Long, Player>() {{
                        put(-1L, new Player());
                    }})
                    .build();

            manager.insert(team);
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
        public void testGet() throws Exception {
            log.info("Test: Get path from database");
            Path expected = new PathBuilder()
                    .setPathId(0)
                    .setCheckpoints(Lists.newArrayList(
                            checkpoints.get(0),
                            checkpoints.get(1)))
                    .build();

            Path actual = manager.get(0);

            assertEquals(expected, actual);
        }

        @Test
        public void testUpdate() throws Exception {
            log.info("Test: Update a path");
            Path original = manager.get(0);

            List<Checkpoint> cps = Lists.newArrayList();
            for (Checkpoint checkpoint : original.getCheckpoints()) {
                cps.add(checkpoint);
            }
            cps.add(cps.get(1));

            Path expected = new PathBuilder(original)
                    .setCheckpoints(cps)
                    .build();

            manager.update(expected);

            Path actual = manager.get(0);

            assertNotSame(original, actual);
            assertEquals(expected, actual);

            // Other tests will depend on an unchanged database so revert changes here :p
            manager.update(original);
        }

        @Test
        public void testInsert() throws Exception {
            log.info("Test: Create new path");

            Path expected = new PathBuilder()
                    .setCheckpoints(Lists.newArrayList(
                            checkpoints.get(1),
                            checkpoints.get(2)))
                    .build();

            long id = manager.insert(expected);

            Path actual = manager.get(id);

            expected.setId(id);

            assertEquals(expected, actual);
        }

        @Test
        public void testDelete() throws Exception {
            log.info("Test: Delete existing path");

            // Create a new item for use in this test
            Path toDelete = new PathBuilder()
                    .setCheckpoints(Lists.newArrayList(
                            checkpoints.get(0),
                            checkpoints.get(1),
                            checkpoints.get(2)))
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

            throw new Exception("Delete path failed.");
        }

        @Test(expected = GeneralServiceException.class)
        public void testGetNonExisting() throws Exception {
            log.info("Test: Get non-existing path");
            manager.get(100);
        }

        public void testUpdateBadData() throws Exception { /* ignore */ }

        public void testInsertBadData() throws Exception { /* ignore */ }
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

            // Other tests will depend on an unchanged database so revert changes here :p
            manager.update(original);
        }

        @Test
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

            throw new Exception("Delete checkpoint failed.");
        }

        @Test(expected = GeneralServiceException.class)
        public void testGetNonExisting() throws Exception {
            log.info("Test: Get non-existing checkpoint");
            manager.get(100);
        }

        @Test(expected = IllegalArgumentException.class)
        public void testUpdateBadData() throws Exception {
            log.info("Test: Update checkpoint with bad data");
            Checkpoint checkpoint = manager.get(0);
            checkpoint.setLatitude(-107.123584f);
            checkpoint.setLongitude(188.185545f);

            manager.update(checkpoint);
        }

        @Test(expected = IllegalArgumentException.class)
        public void testInsertBadData() throws Exception {
            log.info("Test: Create new checkpoint with bad data");
            Checkpoint checkpoint = new CheckpointBuilder()
                    .setLatitude(-107.123584f)
                    .setLongitude(188.185545f)
                    .setChallenge(new ChallengeManager().get(0))
                    .build();

            manager.insert(checkpoint);
        }
    }

////////////////////////////////////////////////////////////////////////////////////////////////////

    public static class ChallengeManagerTest implements ManagerTest {
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
            log.info("Test: Update a challenge");
            Challenge original = manager.get(0);

            Challenge expected = new ChallengeBuilder(original)
                    .setChallengeText("First go there and do this thing, and then you have to go somewhere completely different.")
                    .build();

            manager.update(expected);

            Challenge actual = manager.get(0);

            assertNotSame(original, actual);
            assertEquals(expected, actual);

            // Other tests will depend on an unchanged database so revert changes here :p
            manager.update(original);
        }

        @Test
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

        @Test(expected = GeneralServiceException.class)
        public void testGetNonExisting() throws Exception {
            log.info("Test: Get non-existing challenge");
            manager.get(100);
        }

        public void testUpdateBadData() throws Exception { /* ignore */ }

        public void testInsertBadData() throws Exception { /* ignore */ }
    }

////////////////////////////////////////////////////////////////////////////////////////////////////

    public static class ScavengerHuntManagerTest implements ManagerTest {
        private final ScavengerHuntManager manager = new ScavengerHuntManager();
        private final PathManager paths = new PathManager();
        private final TeamManager teams = new TeamManager();

        @Test
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

            // Other tests will depend on an unchanged database so revert changes here :p
            manager.update(original);
        }

        @Test
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

        @Test(expected = GeneralServiceException.class)
        public void testGetNonExisting() throws Exception {
            log.info("Test: Get non-existing scavenger hunt");
            manager.get(100);
        }

        @Test(expected = IllegalArgumentException.class)
        public void testUpdateBadData() throws Exception {
            log.info("Test: Update existing scavenger hunt with bad data");
            ScavengerHunt sh = manager.get(0);

            // Start time must be before finish time
            sh.setStartTime(new DateTime(2012, 12, 3, 14, 0));
            sh.setFinishTime(new DateTime(2012, 12, 3, 13, 0));

            manager.update(sh);
        }

        @Test(expected = IllegalArgumentException.class)
        public void testInsertBadData() throws Exception {
            log.info("Test: Create new scavenger hunt with bad data");
            ScavengerHunt sh = new ScavengerHuntBuilder()
                    .setPath(new PathManager().get(0))
                    .setTeam(new TeamManager().get(0))
                    .setStartTime(new DateTime(2012, 12, 3, 14, 0))
                    .setFinishTime(new DateTime(2012, 12, 3, 13, 0))
                    .build();

            manager.insert(sh);
        }
    }

////////////////////////////////////////////////////////////////////////////////////////////////////

    private interface ManagerTest {

        public void testGet() throws Exception;

        public void testUpdate() throws Exception;

        public void testInsert() throws Exception;

        public void testDelete() throws Exception;

        public void testGetNonExisting() throws Exception;

        public void testUpdateBadData() throws Exception;

        public void testInsertBadData() throws Exception;
    }
}
