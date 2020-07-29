package de.uol.swp.common.game.messages;

import de.uol.swp.common.message.AbstractServerMessage;

import java.util.UUID;

public class CountdownRefreshMessage extends AbstractServerMessage {

    private final UUID gameID;
    private final int countdown;

    /**
     * Der Konstruktor der CountdownRefreshMessage.
     *
     * @param gameID    Die GameID
     * @param countdown Der aktuelle Countdown
     * @author Keno S.
     * @since Sprint 10
     */
    public CountdownRefreshMessage (UUID gameID, int countdown) {
        this.gameID = gameID;
        this.countdown = countdown;
    }

    /**
     * Gibt die GameID zurück.
     *
     * @return Die GameID
     */
    public UUID getGameID() {
        return gameID;
    }

    /**
     * Gibt den aktuellen Countdown zurück.
     *
     * @return Der Countdown
     */
    public int getCountdown() {
        return countdown;
    }
}
