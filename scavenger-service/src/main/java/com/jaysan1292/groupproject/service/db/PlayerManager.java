package com.jaysan1292.groupproject.service.db;

import com.jaysan1292.groupproject.data.Player;
import com.jaysan1292.groupproject.data.PlayerBuilder;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkArgument;

public class PlayerManager extends AbstractManager<Player> {
    private static final Pattern STUDENT_NUMBER_REGEX = Pattern.compile("([0-9]{9})");
    private static final String TABLE_NAME = "player_t";
    private static final String ID_COLUMN = "player_id";
    private static final String FIRST_NAME_COLUMN = "first_name";
    private static final String LAST_NAME_COLUMN = "last_name";
    private static final String STUDENT_NUMBER_COLUMN = "student_number";

    public PlayerManager() {
        super(Player.class);
    }

    protected String tableName() {
        return TABLE_NAME;
    }

    protected String idColumn() {
        return ID_COLUMN;
    }

    protected Player buildObject(ResultSet rs) throws SQLException {
        return new PlayerBuilder()
                .setPlayerId(rs.getLong(ID_COLUMN))
                .setFirstName(rs.getString(FIRST_NAME_COLUMN))
                .setLastName(rs.getString(LAST_NAME_COLUMN))
                .setStudentId(rs.getString(STUDENT_NUMBER_COLUMN))
                .build();
    }

    protected long doInsert(Player item) throws SQLException {
        validate(item);

        String query = "INSERT INTO " + TABLE_NAME + " (" +
                       FIRST_NAME_COLUMN + ", " +
                       LAST_NAME_COLUMN + ", " +
                       STUDENT_NUMBER_COLUMN + ") VALUES (?, ?, ?)";

        /*
         * For some reason, using ScalarHandler<Long> causes ClassCastException, as the default
         * implementation of ScalarHandler calls getObject() on the ResultSet, which in this case
         * returns java.math.BigDecimal, which can't be casted to Long
         */
        return runner.insert(query,
                             new ScalarHandler<BigDecimal>(1),
                             item.getFirstName(),
                             item.getLastName(),
                             item.getStudentNumber()).longValue();
    }

    protected void doUpdate(Player item) throws SQLException {
        validate(item);

        String query = "UPDATE " + TABLE_NAME + " SET " +
                       FIRST_NAME_COLUMN + "=?, " +
                       LAST_NAME_COLUMN + "=?, " +
                       STUDENT_NUMBER_COLUMN + "=? " +
                       "WHERE " + ID_COLUMN + "=?";
        runner.update(query,
                      item.getFirstName(),
                      item.getLastName(),
                      item.getStudentNumber(),
                      item.getId());
    }

    private static void validate(Player p) {
        checkArgument(STUDENT_NUMBER_REGEX.matcher(p.getStudentNumber()).matches(),
                      "Student number must be 9 numerical digits only, got %s instead.",
                      p.getStudentNumber());
    }
}
