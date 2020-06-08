package de.uol.swp.common.game.messages;

import de.uol.swp.common.game.response.AbstractGameMessage;
import de.uol.swp.common.user.User;

import java.util.UUID;

/**
 * Message, die die Anzahl der Karten auf dem Nachziehstapel an den Spieler sendet und ob der Ablegestapel geleert wurde
 *
 * @author Julia, Fenja, Anna
 * @since Sprint 7
 */
public class CardsDeckSizeMessage extends AbstractGameMessage {

    private static final long serialVersionUID = -1794612026541058915L;
    private int cardsDeckSize;
    private boolean discardPileWasCleared;

    /**
     * Konstruktor der CardDeckSizeMesssage
     *
     * @param gameID        die Game-ID
     * @param player        der Spieler
     * @param cardsDeckSize die Kartendeckgröße
     * @author Julia, Fenja, Anna
     * @since Sprint 7
     */
    public CardsDeckSizeMessage(UUID gameID, User player, int cardsDeckSize) {
        super(gameID, player);
        this.cardsDeckSize = cardsDeckSize;
    }

    /**
     * Überladener Konstruktor der CardDeckSizeMesssage
     *
     * @param gameID                die Game-ID
     * @param player                der Spieler
     * @param cardsDeckSize         die Kartendeckgröße
     * @param discardPileWasCleared ob der Ablagestapel geleert wurde
     * @author Julia, Fenja, Anna
     * @since Sprint 7
     */
    public CardsDeckSizeMessage(UUID gameID, User player, int cardsDeckSize, boolean discardPileWasCleared) {
        super(gameID, player);
        this.cardsDeckSize = cardsDeckSize;
        this.discardPileWasCleared = discardPileWasCleared;
    }

    /**
     * Gibt die Kartendeckgröße zurück
     *
     * @return cardDeckSize die Kartendeckgröße
     * @author Julia, Fenja, Anna
     * @since Sprint 7
     */
    public int getCardsDeckSize() {
        return cardsDeckSize;
    }

    /**
     * Gibt zurück, ob der Ablagestapel geleert wurde.
     *
     * @return discardPileWasCleared die Bestätigung, ob der Ablagestapel geleert wurde
     * @author Julia, Fenja, Anna
     * @since Sprint 7
     */
    public boolean getDiscardPileWasCleared() {
        return discardPileWasCleared;
    }
}


