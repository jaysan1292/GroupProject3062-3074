package com.jaysan1292.groupproject.service.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaysan1292.groupproject.Global;
import com.jaysan1292.groupproject.data.BaseEntity;
import com.jaysan1292.groupproject.exceptions.GeneralServiceException;
import com.jaysan1292.groupproject.exceptions.ItemNotFoundException;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/** @author Jason Recillo */
public abstract class AbstractManager<T extends BaseEntity> {
    protected static final ObjectMapper mapper = new ObjectMapper();
    protected static final QueryRunner runner = new QueryRunner();
    private static final Object lock = new Object();
    private String SELECT_QUERY;
    private String SELECT_ALL_QUERY;
    private String _itemName;
    private Class<T> _cls;

    protected AbstractManager(Class<T> _cls, String tableName, String idColumnName) {
        SELECT_QUERY = "SELECT * FROM " + tableName + " WHERE " + idColumnName + "=?";
        SELECT_ALL_QUERY = "SELECT * FROM " + tableName;

        String[] name = StringUtils.split(_cls.getName(), '.');
        String s = StringUtils.splitByCharacterTypeCamelCase(name[name.length - 1])[0].toLowerCase();

        _itemName = s.equals("scavenger") ? "scavenger hunt" : s;

        this._cls = _cls;
    }

    protected ResultSetHandler<T> getResultSetHandler() {
        return new ResultSetHandler<T>() {
            public T handle(ResultSet rs) throws SQLException {
                if (!rs.next()) return null;

                return buildObject(rs);
            }
        };
    }

    protected ResultSetHandler<T[]> getArrayResultSetHandler() {
        return new ResultSetHandler<T[]>() {
            @SuppressWarnings("unchecked")
            public T[] handle(ResultSet rs) throws SQLException {
                if (!rs.next()) return null;

                ArrayList<T> items = new ArrayList<T>();
                do {
                    items.add(buildObject(rs));
                } while (rs.next());

                return items.toArray((T[]) Array.newInstance(_cls, items.size()));
            }
        };
    }

    protected abstract T createNewInstance();

    protected abstract T buildObject(ResultSet rs) throws SQLException;

    public T get(long id) throws GeneralServiceException {
        Connection connection = null;
        T item = null;
        try {
            synchronized (lock) {
                connection = DatabaseHelper.createDbConnection();
                item = runner.query(connection, SELECT_QUERY, getResultSetHandler(), id);
            }
            if (item == null) throw new ItemNotFoundException(id, _cls);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            DbUtils.closeQuietly(connection);
        }
        Global.log.debug("Retrieved " + _itemName + " #" + item.getId());

        return item;
    }

    public T[] getAll() {
        Connection connection = null;
        T[] items = null;
        try {
            synchronized (lock) {
                connection = DatabaseHelper.createDbConnection();
                items = runner.query(connection, SELECT_ALL_QUERY, getArrayResultSetHandler());
            }

            if (items == null) throw new ItemNotFoundException("There are no " + _itemName + "s in the database.");
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            DbUtils.closeQuietly(connection);
        }

        Global.log.debug("Retrieved all " + _itemName + "s.");

        return items;
    }

    public T create(T item) throws GeneralServiceException {
        Connection conn = null;
        try {
            synchronized (lock) {
                conn = DatabaseHelper.createDbConnection();
                doCreate(conn, item);
            }
        } catch (SQLException e) {
            throw new GeneralServiceException("There was an error updating the database.", e);
        } finally {
            DbUtils.closeQuietly(conn);
        }

        return item;
    }

    public void update(T item) throws GeneralServiceException {
        Connection connection = null;
        try {
            synchronized (lock) {
                connection = DatabaseHelper.createDbConnection();
                doUpdate(connection, item);
            }
        } catch (SQLException e) {
            throw new GeneralServiceException("There was a problem updating the given item.", e);
        } finally {
            DbUtils.closeQuietly(connection);
        }
    }

    public void delete(T item) throws GeneralServiceException {
        Connection conn = null;
        try {
            synchronized (lock) {
                conn = DatabaseHelper.createDbConnection();
                doDelete(conn, item);
            }
        } catch (SQLException e) {
            throw new GeneralServiceException("There was a problem deleting the given item from the database.", e);
        } finally {
            DbUtils.closeQuietly(conn);
        }
    }

    protected abstract void doCreate(Connection conn, T item) throws SQLException;

    protected abstract void doUpdate(Connection conn, T item) throws SQLException;

    protected abstract void doDelete(Connection conn, T item) throws SQLException;
}
