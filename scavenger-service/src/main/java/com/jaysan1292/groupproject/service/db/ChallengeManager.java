package com.jaysan1292.groupproject.service.db;

import com.jaysan1292.groupproject.data.Challenge;
import com.jaysan1292.groupproject.data.ChallengeBuilder;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/** @author Jason Recillo */
public class ChallengeManager extends AbstractManager<Challenge> {
    public ChallengeManager() {
        super(Challenge.class, "Challenge", "challenge_id");
    }

    protected ResultSetHandler<Challenge> getResultSetHandler() {
        return new ResultSetHandler<Challenge>() {
            public Challenge handle(ResultSet rs) throws SQLException {
                if (!rs.next()) return null;

                return new ChallengeBuilder()
                        .setChallengeId(rs.getLong("challenge_id"))
                        .setChallengeText(rs.getString("challenge_text"))
                        .build();
            }
        };
    }

    protected ResultSetHandler<Challenge[]> getArrayResultSetHandler() {
        return new ResultSetHandler<Challenge[]>() {
            public Challenge[] handle(ResultSet rs) throws SQLException {
                if (!rs.next()) return null;

                ArrayList<Challenge> challenges = new ArrayList<Challenge>();
                do {
                    challenges.add(new ChallengeBuilder()
                                           .setChallengeId(rs.getLong("challenge_id"))
                                           .setChallengeText(rs.getString("challenge_text"))
                                           .build());
                } while (rs.next());

                return challenges.toArray(new Challenge[challenges.size()]);
            }
        };
    }

    protected Challenge createNewInstance() {
        return new Challenge();
    }
}
