package com.jaysan1292.groupproject.service.db;

import com.jaysan1292.groupproject.Global;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.io.IOUtils;
import org.apache.derby.tools.ij;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHelper {
    private static final String DB_DRIVER_CLASS;
    private static final String DB_NAME;
    private static final String DB_URL;

    static {
//        DB_DRIVER_CLASS = "org.h2.Driver";
//        DB_NAME = "Database/scavengerdb";
//        DB_URL = "jdbc:h2:" + DB_NAME;
        DB_DRIVER_CLASS = "org.apache.derby.jdbc.EmbeddedDriver";
        DB_NAME = "scavengerdb";
        DB_URL = "jdbc:derby:" + DB_NAME;

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                // Shut down the Derby engine

            }
        });
    }

    private DatabaseHelper() {}

    /** Returns a Connection object that points to the main database. */
    public static Connection createDbConnection() throws SQLException {
        try {
            Class.forName(DB_DRIVER_CLASS);
        } catch (ClassNotFoundException ignored) {}

        try {
            return DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            // if database doesn't exist, create it
            if (e.getSQLState().equals("XJ004")) {
                Global.log.info("Database '" + DB_NAME + "' doesn't exist, so let's create it now.");
                Connection connection = DriverManager.getConnection(DB_URL + ";create=true");
                // Populate its tables with preset data
                InputStream sql = null;
                try {
                    sql = ClassLoader.getSystemResourceAsStream("scavengerdb.sql");
                    // runScript returns the number of SQLExceptions thrown
                    // while executing the script; this should be exactly 0.
                    int errors = ij.runScript(connection, sql, "UTF-8", System.out, "UTF-8");

                    if (errors != 0) throw new RuntimeException("There was an error executing scavengerdb.sql.");
                    Global.log.info("Database creation successful!");
                } catch (UnsupportedEncodingException ignored) {
                } finally {
                    IOUtils.closeQuietly(sql);
                    DbUtils.closeQuietly(connection);
                }
            } else {
                throw e;
            }
        }
        // try once more to create a database connection to use
        return DriverManager.getConnection(DB_URL);
    }
}
