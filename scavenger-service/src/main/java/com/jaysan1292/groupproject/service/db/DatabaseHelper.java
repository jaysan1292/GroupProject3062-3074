package com.jaysan1292.groupproject.service.db;

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
import java.sql.SQLException;

//TODO: Move to DataSource rather than using raw Connection objects
public class DatabaseHelper {
    private static final String DB_DRIVER_CLASS = "org.apache.derby.jdbc.EmbeddedDriver";
    private static final String DB_NAME = "scavengerdb";
    public static final String DB_URL = "jdbc:derby:" + DB_NAME;
    private static EmbeddedDataSource _dataSource;

    public static void initDatabase() throws Exception {
        _dataSource = new EmbeddedDataSource();
        _dataSource.setDatabaseName(DB_NAME);
        _dataSource.setCreateDatabase("create");

        Connection connection = _dataSource.getConnection();
        // Populate its tables with preset data
        InputStream sql = null;
        try {
            sql = ClassLoader.getSystemResourceAsStream("scavengerdb.sql");
            // runScript returns the number of SQLExceptions thrown
            // while executing the script; this should be exactly 0.
            int errors = ij.runScript(connection, sql, "UTF-8", System.out, "UTF-8");

            if (errors != 0)
                throw new RuntimeException("There was an error executing scavengerdb.sql.");
            Global.log.info("Database creation successful!");
        } catch (UnsupportedEncodingException ignored) {
        } finally {
            IOUtils.closeQuietly(sql);
            DbUtils.closeQuietly(connection);
        }
    }

    public static void cleanDatabase() {
        try {
            Global.log.info("Shutting down database.");
            EmbeddedDataSource ds = (EmbeddedDataSource) getDataSource();
            ds.setShutdownDatabase("shutdown");
            ds.getConnection();
        } catch (SQLException e) {
            if ((e.getErrorCode() == 45000) && ("08006".equals(e.getSQLState()))) {
                Global.log.info("Database shutdown successfully!");

                try {
                    Global.log.info("Cleaning database directory.");
                    FileDeleteStrategy.FORCE.delete(new File(DB_NAME));
                    Global.log.info("Database directory cleaned successfully!");
                } catch (IOException e1) {
                    Global.log.error("There was a problem deleting the database directory. Please delete it manually.");
                    Global.log.error(e1.getMessage(), e1);
                }
            } else {
                Global.log.info("Database did not shutdown successfully. Please delete the database directory manually to prevent further errors.");
            }
        }
    }

    private DatabaseHelper() {}

    /** Returns a Connection object that points to the main database. */
    public static Connection createDbConnection() throws SQLException {
        return getDataSource().getConnection();
    }

    public static DataSource getDataSource() {
        return _dataSource;
    }
}
