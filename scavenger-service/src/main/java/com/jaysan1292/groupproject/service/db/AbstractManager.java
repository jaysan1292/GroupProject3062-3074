package com.jaysan1292.groupproject.service.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.jaysan1292.groupproject.Global;
import com.jaysan1292.groupproject.data.BaseEntity;
import com.jaysan1292.groupproject.exceptions.GeneralServiceException;
import com.jaysan1292.groupproject.exceptions.ItemNotFoundException;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/** @author Jason Recillo */
public abstract class AbstractManager<T extends BaseEntity> {
    protected static final ObjectMapper mapper = new ObjectMapper();
    protected static final QueryRunner runner = new QueryRunner(DatabaseHelper.getDataSource());
    private static final Object lock = new Object();
    private String _itemName;
    private Class<T> _cls;

    protected AbstractManager(Class<T> cls) {

        String[] name = StringUtils.split(cls.getName(), '.');
        String s = StringUtils.splitByCharacterTypeCamelCase(name[name.length - 1])[0].toLowerCase();

        _itemName = s.equals("scavenger") ? "scavenger hunt" : s;

        this._cls = cls;
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

                ArrayList<T> items = Lists.newArrayList();
                do {
                    items.add(buildObject(rs));
                } while (rs.next());

                return items.toArray((T[]) Array.newInstance(_cls, items.size()));
            }
        };
    }

    protected abstract String tableName();

    protected abstract String idColumn();

    protected abstract T buildObject(ResultSet rs) throws SQLException;

    /**
     * Retrieves an item with the given ID from the database.
     *
     * @param id The ID of the item to get.
     *
     * @return The item.
     *
     * @throws GeneralServiceException
     */
    public T get(long id) throws GeneralServiceException {
        checkArgument(id >= 0, "ID must be non-negative.");
        T item;
        try {
            synchronized (lock) {
                String query = "SELECT * FROM " + tableName() + " WHERE " + idColumn() + "=?";
                item = runner.query(query, getResultSetHandler(), id);
            }
            if (item == null) {
                throw new GeneralServiceException(new ItemNotFoundException(id, _cls));
            }

            return item;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Gets all items from the given database table.
     *
     * @return An Array containing all of the items in the database.
     */
    public T[] getAll() {
        T[] items;
        try {
            synchronized (lock) {
                String query = "SELECT * FROM " + tableName();
                items = runner.query(query, getArrayResultSetHandler());
            }

            if (items == null)
                throw new ItemNotFoundException("There are no " + _itemName + "s in the database.");
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        Global.log.debug("Retrieved all " + _itemName + "s.");

        return items;
    }

    /**
     * Inserts an item into the database.
     *
     * @param item The item to insert
     *
     * @return The ID of the new object.
     *
     * @throws GeneralServiceException
     */
    public long insert(T item) throws GeneralServiceException {
        checkNotNull(item);
        try {
            synchronized (lock) {
                return doInsert(item);
            }
        } catch (SQLException e) {
            throw new GeneralServiceException("There was an error updating the database.", e);
        }
    }

    public void update(T item) throws GeneralServiceException {
        checkNotNull(item);
        try {
            synchronized (lock) {
                doUpdate(item);
            }
        } catch (SQLException e) {
            throw new GeneralServiceException("There was a problem updating the given item.", e);
        }
    }

    public void delete(T item) throws GeneralServiceException {
        checkNotNull(item);
        try {
            synchronized (lock) {
                doDelete(item);
            }
        } catch (SQLException e) {
            throw new GeneralServiceException("There was a problem deleting the given item from the database.", e);
        }
    }

    protected abstract long doInsert(T item) throws SQLException;

    protected abstract void doUpdate(T item) throws SQLException;

    protected abstract void doDelete(T item) throws SQLException;
}
