package de.uol.swp.common.game.response;

import de.uol.swp.common.user.User;

import java.util.UUID;

abstract public class GameMessage {

    private User player;
    private UUID gameID;
    private short cardID;

    /**
     * Instanziiert GameMessage
     *
     * @param cardID Die ID der Karte
     * @param gameID Die ID des Games
     * @param player Der Spieler
     * @author Paula
     * @version 1
     * @since Sprint5
     */
    public GameMessage(UUID gameID, User player, short cardID) {
        this.gameID = gameID;
        this.player = player;
        this.cardID = cardID;
    }

    /**
     * Gibt GameID zurück
     *
     * @return GameID
     * @author Paula
     * @version 1
     * since Sprint5
     */
    public UUID getGameID() {
        return gameID;
    }

    /**
     * Gibt CardID zurück
     *
     * @return CardID der ausgewählten Karte
     * @author Paula
     * @version 1
     * since Sprint5
     */
    public short getCardID() {
        return cardID;
    }

    /**
     * Gibt den Player zurück
     *
     * @return Spieler, der die Karte ausgewählt hat
     * @author Paula
     * @version 1
     * since Sprint5
     */

    public User getPlayer() {
        return player;
    }
}