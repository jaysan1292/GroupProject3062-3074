package com.jaysan1292.groupproject.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/** @author Jason Recillo */
public class Player extends BaseEntity {
    public static final Player INVALID = new Player(-1, "(null)", "(null)", "(null)");
    /** The player's ID. Corresponds with the ID for this entry in the database. */
    private long playerId;

    /** The student's first name. */
    private String firstName;

    /** The student's last name. */
    private String lastName;

    /** The student's GBC student ID. */
    private String studentNumber;

    public Player() {
        this(INVALID);
    }

    public Player(long pid, String fn, String ln, String sid) {
        playerId = pid;
        studentNumber = sid;
        firstName = fn;
        lastName = ln;
    }

    public Player(Player other) {
        this(other.playerId, other.firstName, other.lastName, other.studentNumber);
    }

    //region JavaBean

    public long getId() {
        return playerId;
    }

    public void setId(long id) {
        setPlayerId(id);
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    protected void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    protected void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    protected void setLastName(String lastName) {
        this.lastName = lastName;
    }

    protected void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    //endregion JavaBean

    @JsonIgnore
    public String getFullName() {
        return firstName + ' ' + lastName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Player)) return false;
        Player other = (Player) obj;
        return (playerId == other.playerId) &&
               (studentNumber.equals(other.studentNumber)) &&
               (firstName.equals(other.firstName)) &&
               (lastName.equals(other.lastName));
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(23, 97)
                .append(playerId)
                .append(studentNumber)
                .append(firstName)
                .append(lastName)
                .toHashCode();
    }
}
