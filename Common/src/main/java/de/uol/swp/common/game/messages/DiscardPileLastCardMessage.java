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
     * @param gameID
     * @param cardID
     * @param user
     * @author Fenja
     * @version 1.0
     * @since Sprint6
     */
    public DiscardPileLastCardMessage(UUID gameID, short cardID, User user) {
        this.gameID = gameID;
        this.cardID = cardID;
        this.user = user;
    }

    //getter
    public User getUser() {
        return user;
    }

    public UUID getGameID() {
        return gameID;
    }

    public short getCardID() {
        return cardID;
    }
}
