package com.jaysan1292.groupproject.service.db;

import com.google.common.base.Preconditions;
import com.jaysan1292.groupproject.Global;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.io.IOUtils;
import org.apache.derby.jdbc.EmbeddedDataSource;
import org.apache.derby.tools.ij;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseHelper {
    private static final String DB_NAME = "scavengerdb";
    public static final String DB_URL = "jdbc:derby:" + DB_NAME;

    private static EmbeddedDataSource _dataSource;
    private static boolean _initialized;

    public static void initDatabase() throws Exception {
        Preconditions.checkState(!_initialized);

        Global.log.debug("Current working directory is: " + System.getProperty("user.dir"));

        // Clear database directory if exists
        removeDatabaseDirectory();

        Global.log.debug("Creating database...");
        _dataSource = new EmbeddedDataSource();
        _dataSource.setDatabaseName(DB_NAME);
        _dataSource.setShutdownDatabase(null);
        _dataSource.setCreateDatabase("create");

        Connection connection;
        try {
            connection = _dataSource.getConnection();
        } catch (SQLException e) {
            // Delete the database directory and try again
            FileDeleteStrategy.FORCE.delete(new File(DB_NAME));
            connection = _dataSource.getConnection();
        }
        // Populate its tables with preset data
        InputStream sql = null;
        try {
            sql = ClassLoader.getSystemResourceAsStream("scavengerdb.sql");
            // runScript returns the number of SQLExceptions thrown
            // while executing the script; this should be exactly 0.
            int errors = ij.runScript(connection, sql, "UTF-8", System.out, "UTF-8");

            if (errors != 0) {
                String errMsg = "There was an error initializing the database.";
                Global.log.error(errMsg);
                _initialized = true;
                cleanDatabase();
                throw new RuntimeException(errMsg);
            }
            Global.log.info("Database creation successful!");
        } catch (UnsupportedEncodingException ignored) {
        } finally {
            IOUtils.closeQuietly(sql);
            DbUtils.closeQuietly(connection);
        }

        _initialized = true;
    }

    public static void cleanDatabase() throws Exception {
        Preconditions.checkState(_initialized);
        try {
            Global.log.info("Shutting down database.");
//            _dataSource.setShutdownDatabase("shutdown");
//            _dataSource.getConnection();
            DriverManager.getConnection("jdbc:derby:" + DB_NAME + ";shutdown=true");
        } catch (SQLException e) {
            // It doesn't really matter whether or not it shuts down properly because it will just
            // be deleted anyways later :p
            if ((e.getErrorCode() == 45000) && ("08006".equals(e.getSQLState()))) {
                Global.log.info("Database shutdown successfully!");
            } else {
                Global.log.error("Database did not shutdown successfully.");
            }
        }

        Thread.sleep(1000); // Wait a bit before deleting database directory

        removeDatabaseDirectory();

        _initialized = false;
    }

    private DatabaseHelper() {}

    public static DataSource getDataSource() {
        Preconditions.checkState(_initialized);
        return _dataSource;
    }

    private static void removeDatabaseDirectory() {
        try {
            Global.log.info("Cleaning database directory.");
            FileDeleteStrategy.FORCE.delete(new File(DB_NAME));
            Global.log.info("Database directory cleaned successfully!");
        } catch (IOException e1) {
            Global.log.error("There was a problem deleting the database directory. Please delete it manually.");
            Global.log.error(e1.getMessage(), e1);
        }
    }
}
