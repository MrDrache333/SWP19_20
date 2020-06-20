package de.uol.swp.common.game.messages;

import de.uol.swp.common.message.AbstractServerMessage;

import java.util.UUID;

public class CountdownRefreshMessage extends AbstractServerMessage {

    private final UUID gameID;
    private final int countdown;

    public CountdownRefreshMessage (UUID gameID, int countdown) {
        this.gameID = gameID;
        this.countdown = countdown;
    }

    public UUID getGameID() {
        return gameID;
    }

    public int getCountdown() {
        return countdown;
    }
}
