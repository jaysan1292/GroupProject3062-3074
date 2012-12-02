package com.jaysan1292.groupproject.data;

import com.jaysan1292.groupproject.service.security.EncryptionUtils;

/** @author Jason Recillo */
public class PlayerBuilder extends AbstractBuilder<Player> {
    private Player player;

    public PlayerBuilder() {
        init();
    }

    public PlayerBuilder(Player player) {
        this.player = new Player(player);
    }

    protected void init() {
        player = new Player();
    }

    public Player build() {
        return player;
    }

    public PlayerBuilder setPlayerId(long playerId) {
        player.setPlayerId(playerId);
        return this;
    }

    public PlayerBuilder setFirstName(String firstName) {
        player.setFirstName(firstName);
        return this;
    }

    public PlayerBuilder setLastName(String lastName) {
        player.setLastName(lastName);
        return this;
    }

    public PlayerBuilder setStudentId(String studentId) {
        player.setStudentNumber(studentId);
        return this;
    }

    public PlayerBuilder setPassword(String encryptedPassword) {
        player.setPassword(encryptedPassword);
        return this;
    }

    public PlayerBuilder setPasswordUnencrypted(String plainPassword) {
        player.setPassword(EncryptionUtils.encryptPassword(plainPassword));
        return this;
    }

    public PlayerBuilder setAdmin(boolean admin) {
        player.setAdmin(admin);
        return this;
    }
}
