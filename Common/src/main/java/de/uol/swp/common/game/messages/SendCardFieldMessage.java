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
    private final UUID gameID;
    private final Map<Short, Integer> cardField;

    /**
     * Der Konstruktor der SendCardFieldMessage
     *
     * @param gameID    die Karten-ID
     * @param cardField das Kartenfeld
     * @author Fenja, Anna
     * @since Sprint 7
     */
    public SendCardFieldMessage(UUID gameID, Map<Short, Integer> cardField) {
        this.gameID = gameID;
        this.cardField = cardField;
    }

    /**
     * Gibt das Kartenfeld zurück
     *
     * @return cardField das Kartenfeld
     * @author Fenja, Anna
     * @since Sprint 7
     */
    public Map<Short, Integer> getCardField() {
        return cardField;
    }

    /**
     * Gibt die Game-ID zurück
     *
     * @return gameID die Game-ID
     * @author Fenja, Anna
     * @since Sprint 7
     */
    public UUID getGameID() {
        return gameID;
    }
}
