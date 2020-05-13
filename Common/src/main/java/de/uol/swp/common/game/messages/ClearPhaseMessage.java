package de.uol.swp.common.game.messages;

import de.uol.swp.common.message.AbstractServerMessage;
import de.uol.swp.common.user.User;

import java.util.ArrayList;
import java.util.UUID;

public class ClearPhaseMessage extends AbstractServerMessage {

    /**
     * Die Message die anderen Spielern gesendet wird, wenn ein Spieler eine Karte entsorgt.
     *
     * @param gameID                Die ID des aktuellen Spiels
     * @param currentUser           Der Spieler der gerade am Zug ist
     * @param UserPlaceNumber       Der Index des Users in der Players Liste
     * @param enemyPlaceNumber      Der Index des Gegners in der Player Liste
     * @author Devin
     * @since Sprint 7
     */

    private final UUID gameID;
    private final User currentUser;
    private final Short userPlaceNumber;
    private final Short enemyPlaceNumber;
    private ArrayList<Short> cardsToDraw;

    public ClearPhaseMessage (UUID gameID, User currentUser, Short userPlaceNumber, Short enemyPlaceNumber, ArrayList<Short> cardsToDraw) {
        this.gameID = gameID;
        this.currentUser = currentUser;
        this.userPlaceNumber = userPlaceNumber;
        this.enemyPlaceNumber = enemyPlaceNumber;
        this.cardsToDraw = cardsToDraw;

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

    public ArrayList<Short> getCardsToDraw() {
        return cardsToDraw;
    }
}
