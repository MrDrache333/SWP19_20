package de.uol.swp.common.game.messages;

import de.uol.swp.common.message.AbstractServerMessage;
import de.uol.swp.common.user.User;

import java.util.UUID;


public class StartClearPhaseMessage extends AbstractServerMessage {

    /**
     * Message die signalisiert, dass sich der User jetzt in der Clearphase befindet
     * @param gameID                Die ID des aktuellen Spiels
     * @param currentUser           Der Spieler der gerade am Zug ist
     * @param UserPlaceNumber       Der Index des Users in der Players Liste
     * @param enemyPlaceNumber      Der Index des Gegners in der Player Liste
     * @author Julia, Devin
     * @since Sprint5,
     */

    private static final long serialVersionUID = -4262409792917076822L;
    private UUID gameID;
    private User currentUser;
    private Short userPlaceNumber;
    private Short enemyPlaceNumber;

    public StartClearPhaseMessage(User currentUser, UUID gameID, Short userPlaceNumber, Short enemyPlaceNumber) {
        this.gameID = gameID;
        this.currentUser = currentUser;
        this.userPlaceNumber = userPlaceNumber;
        this.enemyPlaceNumber = enemyPlaceNumber;
    }

    public UUID getGameID() {
        return gameID;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public Short getEnemyPlaceNumber() {
        return enemyPlaceNumber;
    }

    public Short getUserPlaceNumber() {
        return userPlaceNumber;
    }

}
