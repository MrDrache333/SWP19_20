package de.uol.swp.common.game.request;

import de.uol.swp.common.message.AbstractRequestMessage;
import de.uol.swp.common.user.User;

import java.util.UUID;

public class CancelPoopBreakRequest extends AbstractRequestMessage {

    private final User user;
    private final UUID gameID;

    /**
     * Der Konstruktor des CancelPoopBreakRequests.
     *
     * @param user Der User
     * @param gameID Die GameID
     * @author Keno S.
     * @since Sprint 10
     */
    public CancelPoopBreakRequest(User user, UUID gameID) {
        this.user = user;
        this.gameID = gameID;
    }

    /**
     * Gibt den User zurück.
     *
     * @return Der User
     */
    public User getUser() {
        return user;
    }

    /**
     * Gibt die GameID zurück.
     *
     * @return Die GameID
     */
    public UUID getGameID() {
        return gameID;
    }
}
