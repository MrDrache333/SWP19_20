package de.uol.swp.common.game.messages;

import de.uol.swp.common.game.response.AbstractGameMessage;
import de.uol.swp.common.user.User;

import java.util.UUID;

/**
 * Message, die die Anzahl der Karten auf dem Nachziehstapel an den Spieler sendet und ob der Ablegestapel geleert wurde
 *
 * @author Julia, Fenja, Anna
 * @since Sprint7
 */
public class CardsDeckSizeMessage extends AbstractGameMessage {

    private static final long serialVersionUID = -1794612026541058915L;
    private int cardsDeckSize;
    private boolean discardPileWasCleared;

    public CardsDeckSizeMessage(UUID gameID, User player, int cardsDeckSize) {
        super(gameID, player);
        this.cardsDeckSize = cardsDeckSize;
    }

    public CardsDeckSizeMessage(UUID gameID, User player, int cardsDeckSize, boolean discardPileWasCleared) {
        super(gameID, player);
        this.cardsDeckSize = cardsDeckSize;
        this.discardPileWasCleared = discardPileWasCleared;
    }

    public int getCardsDeckSize() {
        return cardsDeckSize;
    }

    public boolean getDiscardPileWasCleared() {
        return discardPileWasCleared;
    }
}


