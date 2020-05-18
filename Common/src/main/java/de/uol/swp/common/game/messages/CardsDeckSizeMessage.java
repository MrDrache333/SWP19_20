package de.uol.swp.common.game.messages;

import de.uol.swp.common.game.response.AbstractGameMessage;
import de.uol.swp.common.user.User;

import java.util.UUID;

/**
 * Message, die die Anzahl der Karten auf dem Nachziehstapel an den Spieler sendet
 *
 * @author Julia
 * @since Sprint7
 */
public class CardsDeckSizeMessage extends AbstractGameMessage {

    private static final long serialVersionUID = -1794612026541058915L;
    private int size;

    public CardsDeckSizeMessage(UUID gameID, User player, int size) {
        super(gameID, player);
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
