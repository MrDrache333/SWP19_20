package de.uol.swp.common.game.messages;

import de.uol.swp.common.message.AbstractServerMessage;

import java.util.Map;
import java.util.UUID;

/**
 * Message, in der dem Client das Kartenfeld geschickt wird
 *
 * @author Fenja, Anna
 * @since Sprint 7
 */
public class SendCardFieldMessage extends AbstractServerMessage {

    private static final long serialVersionUID = 1906869054271279251L;
    private UUID gameID;
    private Map<Short, Integer> cardField;

    public SendCardFieldMessage(UUID gameID, Map<Short, Integer> cardField) {
        this.gameID = gameID;
        this.cardField = cardField;
    }

    public Map<Short, Integer> getCardField() {
        return cardField;
    }

    public void setCardField(Map<Short, Integer> cardField) {
        this.cardField = cardField;
    }

    public UUID getGameID() {
        return gameID;
    }

    public void setGameID(UUID gameID) {
        this.gameID = gameID;
    }
}
