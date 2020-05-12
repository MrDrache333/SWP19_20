package de.uol.swp.common.game.messages;

import de.uol.swp.common.message.AbstractServerMessage;
import de.uol.swp.common.user.User;

import java.util.UUID;

public class OtherPlayerDiscardMessage extends AbstractServerMessage {

    /**
     * Die Message die anderen Spielern gesendet wird, wenn ein Spieler eine Karte entsorgt.
     *
     * @param gameID                Die ID des aktuellen Spiels
     * @param currentUser           Der Spieler der gerade am Zug ist
     * @param cardID                Die ID der entsorgten Karte
     * @param UserPlaceNumber       Der Index des Users in der Players Liste
     * @param enemyPlaceNumber      Der Index des Gegners in der Player Liste
     * @author Devin
     * @since Sprint 7
     */

    private final UUID gameID;
    private final User currentUser;
    private final Short cardID;
    private final Short userPlaceNumber;
    private final Short enemyPlaceNumber;

    public OtherPlayerDiscardMessage (UUID gameID, User currentUser, Short cardID, Short userPlaceNumber, Short enemyPlaceNumber) {
            this.gameID = gameID;
            this.currentUser = currentUser;
            this.cardID = cardID;
            this.userPlaceNumber = userPlaceNumber;
            this.enemyPlaceNumber = enemyPlaceNumber;

    }

    public UUID getGameID() {
        return gameID;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public Short getCardID() {
        return cardID;
    }

    public Short getEnemyPlaceNumber() {
        return enemyPlaceNumber;
    }

    public Short getUserPlaceNumber() {
        return userPlaceNumber;
    }

}
