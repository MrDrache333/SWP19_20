package de.uol.swp.common.game.messages;

import de.uol.swp.common.message.AbstractServerMessage;
import de.uol.swp.common.user.User;

import java.util.UUID;

public class DiscardPileLastCardMessage extends AbstractServerMessage {
    private static final long serialVersionUID = -2520194284028927062L;

    private UUID gameID;
    private short cardID;
    private User user;

    /**
     * Konstruktor, welcher die KartenID der letzten Karte vom Ablegestapel übergibt.
     *
     * @param gameID die Game-ID
     * @param cardID die Karten-ID
     * @param user   der User
     * @author Fenja
     * @since Sprint 6
     */
    public DiscardPileLastCardMessage(UUID gameID, short cardID, User user) {
        this.gameID = gameID;
        this.cardID = cardID;
        this.user = user;
    }

    /**
     * Gibt den User zurück
     *
     * @return user der User
     * @author Fenja
     * @since Sprint 6
     */
    public User getUser() {
        return user;
    }

    /**
     * Gibt die Game-ID zurück
     *
     * @return gameID die Game-ID
     * @author Fenja
     * @since Sprint 6
     */
    public UUID getGameID() {
        return gameID;
    }

    /**
     * Gibt die Karten-ID zurück
     *
     * @return cardID die Karten-ID
     * @author Fenja
     * @since Sprint 6
     */
    public short getCardID() {
        return cardID;
    }
}
