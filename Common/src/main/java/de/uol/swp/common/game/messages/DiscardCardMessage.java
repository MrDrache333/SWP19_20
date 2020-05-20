package de.uol.swp.common.game.messages;

import de.uol.swp.common.message.AbstractServerMessage;
import de.uol.swp.common.user.User;

import java.util.List;
import java.util.UUID;

public class DiscardCardMessage extends AbstractServerMessage {

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
    private final List<Short> cardID;
    private final Short userPlaceNumber;
    private final Short enemyPlaceNumber;
    private final Short numberOfCardsToDiscard;

    public DiscardCardMessage (UUID gameID, User currentUser, List<Short> cardID, Short userPlaceNumber, Short enemyPlaceNumber, Short numberOfCardsToDiscard) {
        this.gameID = gameID;
        this.currentUser = currentUser;
        this.cardID = cardID;
        this.userPlaceNumber = userPlaceNumber;
        this.enemyPlaceNumber = enemyPlaceNumber;
        this.numberOfCardsToDiscard = numberOfCardsToDiscard;

    }

    public UUID getGameID() {
        return gameID;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public List<Short> getCardID() {
        return cardID;
    }

    public Short getEnemyPlaceNumber() {
        return enemyPlaceNumber;
    }

    public Short getUserPlaceNumber() {
        return userPlaceNumber;
    }
}
