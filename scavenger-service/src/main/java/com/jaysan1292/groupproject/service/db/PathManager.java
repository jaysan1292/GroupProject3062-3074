package com.jaysan1292.groupproject.service.db;

import com.jaysan1292.groupproject.data.Path;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/** @author Jason Recillo */
public class PathManager extends AbstractManager<Path> {
    public PathManager() {
        super(Path.class, "Path", "path_id");
    }

    protected Path createNewInstance() {
        return new Path();
    }

    protected Path buildObject(ResultSet rs) {
        return null;  //TODO: Auto-generated method stub
    }

    protected void doCreate(Connection conn, Path item) throws SQLException {
        //TODO: Auto-generated method stub
    }

    protected void doUpdate(Connection conn, Path item) throws SQLException {
        //TODO: Auto-generated method stub
    }

    protected void doDelete(Connection conn, Path item) throws SQLException {
        //TODO: Auto-generated method stub
    }
}
