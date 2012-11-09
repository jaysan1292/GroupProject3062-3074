package com.jaysan1292.groupproject.service.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaysan1292.groupproject.Global;
import com.jaysan1292.groupproject.data.BaseEntity;
import com.jaysan1292.groupproject.data.JSONSerializable;
import com.jaysan1292.groupproject.exceptions.GeneralServiceException;
import com.jaysan1292.groupproject.exceptions.ItemNotFoundException;
import com.jaysan1292.groupproject.util.ArrayUtils;
import com.jaysan1292.groupproject.util.JsonMap;
import com.jaysan1292.groupproject.util.RegexUtils;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/** @author Jason Recillo */
public abstract class AbstractManager<T extends BaseEntity> {
    protected static final ObjectMapper mapper = new ObjectMapper();
    private static final Object lock = new Object();
    private String SELECT_QUERY;
    private String SELECT_ALL_QUERY;
    private String UPDATE_QUERY_TEMPLATE;
    private String itemName;
    private String tableName;
    private Class<T> itemClass;

    protected AbstractManager(Class<T> itemClass, String tableName, String idColumnName) {
        SELECT_QUERY = "SELECT * FROM " + tableName + " WHERE " + idColumnName + "=?";
        SELECT_ALL_QUERY = "SELECT * FROM " + tableName;
        UPDATE_QUERY_TEMPLATE = "UPDATE " + tableName + " SET %s WHERE " + idColumnName + "=?";

        String[] name = StringUtils.split(itemClass.getName(), '.');
        String s = StringUtils.splitByCharacterTypeCamelCase(name[name.length - 1])[0].toLowerCase();

        itemName = s.equals("scavenger") ? "scavenger hunt" : s;

        this.itemClass = itemClass;
        this.tableName = tableName;
    }

    protected abstract ResultSetHandler<T> getResultSetHandler();

    protected abstract ResultSetHandler<T[]> getArrayResultSetHandler();

    protected abstract T createNewInstance();

    public T get(long id) throws ItemNotFoundException {
        QueryRunner runner = new QueryRunner();

        Connection connection = null;
        T item = null;
        try {
            synchronized (lock) {
                connection = DatabaseHelper.createDbConnection();
                item = runner.query(connection, SELECT_QUERY, getResultSetHandler(), id);
            }
            if (item == null) throw new ItemNotFoundException(id, itemClass);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            DbUtils.closeQuietly(connection);
        }
        Global.log.debug("Retrieved " + itemName + " #" + item.getId());

        return item;
    }

    public T[] getAll() {
        QueryRunner runner = new QueryRunner();

        Connection connection = null;
        T[] items = null;
        try {
            synchronized (lock) {
                connection = DatabaseHelper.createDbConnection();
                items = runner.query(connection, SELECT_ALL_QUERY, getArrayResultSetHandler());
            }

            if (items == null) throw new ItemNotFoundException("There are no " + itemName + "s in the database.");
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            DbUtils.closeQuietly(connection);
        }

        Global.log.debug("Retrieved all " + itemName + "s.");

        return items;
    }

    public T create(String json) throws GeneralServiceException {
        T item = createNewInstance();

        try {
            item = item.readJSON(json);
        } catch (Exception e) {
            throw new GeneralServiceException("There was a problem parsing the given JSON string.", e);
        }

        Global.log.debug(item.toString());

        // TODO: Insert item into database

        return item;
    }

    public List<T> batchCreate(Class<T> cls, String json) throws GeneralServiceException {
        ArrayList<T> items = null;
        try {
            items = JSONSerializable.readJSONArray(cls, json);
        } catch (IOException e) {
            throw new GeneralServiceException(e.getMessage(), e);
        }

        Global.log.debug(Arrays.deepToString(items.toArray()));

        return items;
    }

    public void update(long id, JsonMap changedValues) throws SQLException {
        Global.log.debug(changedValues);

        ArrayList<String> columns = new ArrayList<String>();
        ArrayList<String> values = new ArrayList<String>();
        for (Map.Entry<String, Object> entry : changedValues) {
            columns.add(RegexUtils.camelCaseToUnderscore(entry.getKey()) + "=?");
            values.add(String.valueOf(entry.getValue()));
        }

        Global.log.debug("Columns:");
        for (String column : columns) {
            Global.log.debug("    " + column);
        }
        Global.log.debug("Values:");
        for (String value : values) {
            Global.log.debug("    " + value);
        }

        String query = String.format(UPDATE_QUERY_TEMPLATE, StringUtils.join(columns, ", "));
        Global.log.debug(query);

        QueryRunner runner = new QueryRunner();
        Connection connection = null;
        try {
            connection = DatabaseHelper.createDbConnection();
            Object[] params = ArrayUtils.append(values.toArray(), id);
            int rows = runner.update(connection, query, params);
            Global.log.info("Successfully updated " + rows + " rows in the " + tableName + " table!");
        } finally {
            DbUtils.closeQuietly(connection);
        }
    }

    //TODO: delete
}
