package de.uol.swp.common.game;

import de.uol.swp.common.message.AbstractServerMessage;
import de.uol.swp.common.user.User;

import java.util.UUID;

/**
 * The type AbstractGameMessage.
 */
abstract public class AbstractGameMessage extends AbstractServerMessage {

    private static final long serialVersionUID = 5242147448456127805L;
    /**
     * Der Spieler, für den die Nachricht ist
     */
    private final User player;
    /**
     * Die ID des Spiels
     */
    private final UUID gameID;

    /**
     * Instanziiert GameMessage
     *
     * @param gameID Die ID des Games
     * @param player Der Spieler
     * @author Paula, KenoO
     * @since Sprint 7
     */
    public AbstractGameMessage(UUID gameID, User player) {
        this.gameID = gameID;
        this.player = player;
    }

    /**
     * Gibt GameID zurück
     *
     * @return GameID game id
     * @author Paula
     * @version 1  since Sprint5
     * @since
     */
    public UUID getGameID() {
        return gameID;
    }

    /**
     * Gibt den Player zurück
     *
     * @return Spieler, der die Karte ausgewählt hat
     * @author Paula
     * @version 1  since Sprint5
     * @since
     */
    public User getPlayer() {
        return player;
    }
}