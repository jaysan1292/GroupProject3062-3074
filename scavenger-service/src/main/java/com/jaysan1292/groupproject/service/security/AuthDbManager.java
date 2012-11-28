package com.jaysan1292.groupproject.service.security;

/** @author Jason Recillo */
final class AuthDbManager {

//    private static final String ADD_QUERY = "INSERT INTO Authentication VALUES(?, ?);";
//    private static final String GET_QUERY = "SELECT * FROM Authentication WHERE user_id='?';";
//    private static final String REMOVE_QUERY = "DELETE FROM Authentication WHERE auth_token='?';";

    private AuthDbManager() {}

//    public static void add(String authToken, String userId) {
//        Connection connection = null;
//        try {
//            connection = DatabaseHelper.createDbConnection();
//            QueryRunner runner = new QueryRunner();
//            runner.update(connection, ADD_QUERY, authToken, userId);
//        } catch (SQLException e) {
//            Global.log.error(e.getMessage(), e);
//        } finally {
//            DbUtils.closeQuietly(connection);
//        }
//    }
//
//    public static String get(String userId) {
//        ResultSetHandler<String> handler = new ResultSetHandler<String>() {
//            public String handle(ResultSet rs) throws SQLException {
//                if (!rs.next()) {
//                    return null;
//                }
//
//                return rs.getString("auth_token");
//            }
//        };
//
//        Connection connection = null;
//        try {
//            connection = DatabaseHelper.createDbConnection();
//            QueryRunner runner = new QueryRunner();
//            return runner.query(connection, GET_QUERY, handler, userId);
//        } catch (SQLException e) {
//            Global.log.error(e.getMessage(), e);
//            return null;
//        } finally {
//            DbUtils.closeQuietly(connection);
//        }
//    }
//
//    public static void remove(String authToken) {
//        Connection connection = null;
//        try {
//            connection = DatabaseHelper.createDbConnection();
//            QueryRunner runner = new QueryRunner();
//            runner.update(REMOVE_QUERY, authToken);
//        } catch (SQLException e) {
//            Global.log.error(e.getMessage(), e);
//        } finally {
//            DbUtils.closeQuietly(connection);
//        }
//    }
}
