package com.jaysan1292.groupproject.service.db;

import com.jaysan1292.groupproject.data.ScavengerHunt;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/** @author Jason Recillo */
public class ScavengerHuntManager extends AbstractManager<ScavengerHunt> {

    public ScavengerHuntManager() {
        super(ScavengerHunt.class, "ScavengerHunt", "scavenger_hunt_id");
    }

    protected ScavengerHunt createNewInstance() {
        return new ScavengerHunt();
    }

    protected ScavengerHunt buildObject(ResultSet rs) throws SQLException {
        return null;  //TODO: Auto-generated method stub
    }

    protected void doCreate(Connection conn, ScavengerHunt item) throws SQLException {
        //TODO: Auto-generated method stub
    }

    protected void doUpdate(Connection conn, ScavengerHunt item) throws SQLException {
        //TODO: Auto-generated method stub
    }

    protected void doDelete(Connection conn, ScavengerHunt item) throws SQLException {
        //TODO: Auto-generated method stub
    }
}
