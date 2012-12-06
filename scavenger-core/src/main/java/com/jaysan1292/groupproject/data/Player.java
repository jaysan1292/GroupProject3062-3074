package com.jaysan1292.groupproject.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/** @author Jason Recillo */
public class Player extends BaseEntity {
    public static final Player INVALID = new Player(-1, "(null)", "(null)", "(null)", "(null)", false);

    /** The player's ID. Corresponds with the ID for this entry in the database. */
    private long playerId;

    /** The student's first name. */
    private String firstName;

    /** The student's last name. */
    private String lastName;

    /** The student's GBC student ID. */
    private String studentNumber;

    /** The user's password, MD5 hashed. (NEVER STORE PLAIN PASSWORD) */
    private String password;

    /** Whether or not this user is an admin or not */
    private boolean admin;

    public Player() {
        this(INVALID);
    }

    public Player(long playerId, String firstName, String lastName,
                  String studentNumber, String password, boolean admin) {
        this.playerId = playerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.studentNumber = studentNumber;
        this.password = password;
        this.admin = admin;
    }

    public Player(Player other) {
        this(other.playerId, other.firstName, other.lastName,
             other.studentNumber, other.password, other.admin);
    }

    //region JavaBean

    public long getId() {
        return playerId;
    }

    public void setId(long id) {
        playerId = id;
    }

    public String getDescription() {
        return String.format("Player #%d: %s",
                             playerId,
                             getFullName());
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

    public String getPassword() {
        return password;
    }

    @JsonIgnore
    public boolean isAdmin() {
        return admin;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonIgnore
    public void setAdmin(boolean admin) {
        this.admin = admin;
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
               (lastName.equals(other.lastName)) &&
               (admin == other.admin);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(23, 97)
                .append(playerId)
                .append(studentNumber)
                .append(firstName)
                .append(lastName)
                .append(admin)
                .toHashCode();
    }
}
