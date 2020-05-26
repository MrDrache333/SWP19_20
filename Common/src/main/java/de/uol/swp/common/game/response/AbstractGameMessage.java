package de.uol.swp.common.game.response;

import de.uol.swp.common.message.AbstractServerMessage;
import de.uol.swp.common.user.User;

import java.util.UUID;

abstract public class AbstractGameMessage extends AbstractServerMessage {

    private User player;
    private UUID gameID;
    private short cardID;

    /**
     * Basiskonstruktor der AbstractGameMessage
     *
     * @author Devin 6
     * @since Sprint
     */
    public AbstractGameMessage() {
    }

    /**
     * Instanziiert GameMessage
     *
     * @param gameID Die ID des Games
     * @param player Der Spieler
     * @author Paula
     * @since Sprint5
     */
    public AbstractGameMessage(UUID gameID, User player) {
        this.gameID = gameID;
        this.player = player;
    }

    /**
     * Überladener Konstruktor
     * Instanziiert GameMessage
     *
     * @param cardID Die ID der Karte
     * @param gameID Die ID des Games
     * @param player Der Spieler
     * @author Paula
     * @since Sprint5
     */
    public AbstractGameMessage(UUID gameID, User player, short cardID) {
        this.gameID = gameID;
        this.player = player;
        this.cardID = cardID;
    }

    /**
     * Gibt Game-ID zurück
     *
     * @return GameID
     * @author Paula
     * @since Sprint5
     */
    public UUID getGameID() {
        return gameID;
    }

    /**
     * Gibt Card-ID zurück
     *
     * @return CardID der ausgewählten Karte
     * @author Paula
     * @since Sprint5
     */
    public short getCardID() {
        return cardID;
    }

    /**
     * Gibt den Player zurück
     *
     * @return Spieler, der die Karte ausgewählt hat
     * @author Paula
     * @since Sprint5
     */
    public User getPlayer() {
        return player;
    }
}