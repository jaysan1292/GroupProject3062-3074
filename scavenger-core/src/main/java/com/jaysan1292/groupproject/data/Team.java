package com.jaysan1292.groupproject.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.jaysan1292.groupproject.exceptions.ItemNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/** @author Jason Recillo */
public class Team extends BaseEntity {
    private static final Logger log = Logger.getLogger(Team.class);
    public static final Team INVALID = new Team(-1, new HashMap<Long, Player>());

    /** The team ID. Corresponds with the ID for this entry in the database. */
    private long teamId;

    /** A {@link java.util.Map} containing each member on the team. The key is each member's player ID. */
    private Map<Long, Player> teamMembers;

    public Team() {
        this(INVALID);
    }

    public Team(long teamId, Map<Long, Player> teamMembers) {
        this.teamId = teamId;
        this.teamMembers = Maps.newHashMap(teamMembers);
    }

    public Team(Team other) {
        this(other.teamId, other.teamMembers);
    }

    //region JavaBean

    /**
     * Teams must consist of two or more people.
     * If the team size is too small, this method will return false.
     */
    @JsonIgnore
    public boolean isValid() {
        return teamMembers.size() >= 2;
    }

    public long getId() {
        return teamId;
    }

    public void setId(long id) {
        teamId = id;
    }

    public Map<Long, Player> getTeamMembers() {
        return teamMembers;
    }

    protected void setTeamId(long teamId) {
        this.teamId = teamId;
    }

    public void setTeamMembers(Map<Long, Player> teamMembers) {
        this.teamMembers = new HashMap<Long, Player>(teamMembers);
    }

    //endregion JavaBean

    public Player getPlayer(long id) {
        return teamMembers.get(id);
    }

    public Team addMember(Player member) {
        teamMembers.put(member.getId(), member);
        log.info(String.format("Added Player #%d (%s) to Team #%d.", member.getId(), member.getFullName(), teamId));
        return this;
    }

    public Team removeMember(Player member) throws ItemNotFoundException {
        if (!teamMembers.containsValue(member))
            throw new ItemNotFoundException("Player #%d is not part of Team #%d.", member.getId(), teamId);
        teamMembers.remove(member.getId());
        return this;
    }

    @JsonIgnore
    public String getTeamPlayerString() {
        Long[] ids = new Long[teamMembers.size()];

        for (int i = 0; i < ids.length; i++) {
            ids[i] = Iterables.get(teamMembers.values(), i).getId();
        }

        return StringUtils.join(ids, ',');
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Team)) return false;
        Team other = (Team) obj;
        return (teamId == other.teamId) &&
               (teamMembers.equals(other.teamMembers));
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(95, 13)
                .append(teamId)
                .append(teamMembers)
                .toHashCode();
    }
}
