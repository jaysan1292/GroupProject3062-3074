package com.jaysan1292.groupproject.service.accessors;

import com.jaysan1292.groupproject.Global;
import com.jaysan1292.groupproject.data.JSONSerializable;
import com.jaysan1292.groupproject.data.Player;
import com.jaysan1292.groupproject.service.db.PlayerManager;
import com.sun.jersey.api.client.WebResource;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * Date: 26/11/12
 * Time: 12:51 PM
 *
 * @author Jason Recillo
 */
@SuppressWarnings("ZeroLengthArrayAllocation")
public class PlayerAccessorTest extends BaseAccessorTest {
    private final WebResource playerRes = resource.path("players");
    private final PlayerManager playerManager = new PlayerManager();

    @Test
    public void testGetAll() throws Exception {
        Global.log.info("Test: Get all players");

        ArrayList<Player> players = JSONSerializable.readJSONArray(Player.class, playerRes.get(String.class));
        Global.log.info(players);

        assertTrue(players.get(0).getStudentNumber().equals("100726948"));
    }

    @Test
    public void testGet() throws Exception {
        Global.log.info("Test: Get specific player");
        Player expected = playerManager.get(0);

        Player actual = new Player().readJSON(playerRes.path("0").get(String.class));

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
