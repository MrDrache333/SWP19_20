package de.uol.swp.common.game.messages;

import de.uol.swp.common.message.AbstractServerMessage;
import de.uol.swp.common.user.User;

import java.util.UUID;


public class StartClearPhaseMessage extends AbstractServerMessage {

    private static final long serialVersionUID = -4262409792917076822L;
    private final UUID gameID;
    private final User currentUser;
    private final Short userPlaceNumber;
    private final Short enemyPlaceNumber;

    /**
     * Message die signalisiert, dass sich der User jetzt in der Clearphase befindet
     *
     * @param gameID           Die ID des aktuellen Spiels
     * @param currentUser      Der Spieler der gerade am Zug ist
     * @param userPlaceNumber  Der Index des Users in der Players Liste
     * @param enemyPlaceNumber Der Index des Gegners in der Player Liste
     * @author Julia, Devin
     * @since Sprint 5,
     */
    public StartClearPhaseMessage(User currentUser, UUID gameID, Short userPlaceNumber, Short enemyPlaceNumber) {
        this.gameID = gameID;
        this.currentUser = currentUser;
        this.userPlaceNumber = userPlaceNumber;
        this.enemyPlaceNumber = enemyPlaceNumber;
    }

    /**
     * Gibt die Game-ID zurück
     *
     * @return gameID die GameID
     * @author Julia
     * @since Sprint 5
     */
    public UUID getGameID() {
        return gameID;
    }

    /**
     * Gibt den User zurück
     *
     * @return currentUser den aktuellen User
     * @author Devin
     * @since Sprint 7
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Gibt den Index des Gegners in der Players Liste zurück
     *
     * @return enemyPlaceNumber der Index des Gegners in der Players Liste
     * @author Devin
     * @since Sprint 7
     */
    public Short getEnemyPlaceNumber() {
        return enemyPlaceNumber;
    }

    /**
     * Gibt den Index des Users in der Players Liste zurück
     *
     * @return userPlaceNumber der Index des Users in der Players Liste
     * @author Devin
     * @since Sprint 7
     */
    public Short getUserPlaceNumber() {
        return userPlaceNumber;
    }
}
