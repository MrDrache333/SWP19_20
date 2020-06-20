package de.uol.swp.common.game.messages;

import de.uol.swp.common.message.AbstractServerMessage;
import de.uol.swp.common.user.User;

import java.util.UUID;

public class CancelPoopBreakMessage extends AbstractServerMessage {

    private final UUID gameID;
    private final User user;

    /**
     * Der Konstruktor der CancelPoopBreakMessage.
     *
     * @param gameID Die GameID
     * @param user Der User
     * @author Keno S.
     * @since Sprint 10
     */
    public CancelPoopBreakMessage (UUID gameID, User user) {
        this.gameID = gameID;
        this.user = user;
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
     * Gibt den User zurück.
     *
     * @return Der User
     */
    public User getUser() {
        return user;
    }
}
