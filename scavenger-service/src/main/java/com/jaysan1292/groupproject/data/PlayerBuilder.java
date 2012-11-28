package com.jaysan1292.groupproject.data;

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
}
