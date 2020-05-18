package de.uol.swp.common.game.messages;

import de.uol.swp.common.message.AbstractServerMessage;
import de.uol.swp.common.user.User;

import java.util.UUID;

public class BuyCardMessage extends AbstractServerMessage {

    /**
     * Die Antwort auf die BuyCardRequest
     *
     * @param lobbyID       die LobbyID
     * @param currentUser   der User der die Request gestellt hat
     * @param cardID        die ID der Karte (String)
     * @param cardImage     die ImageView der Karte
     * @param counterCard   Anzahl der Karten (die die selbe ID haben) die man noch kaufen kann
     * @param costCard      die Kosten der Karte
     * @author Rike
     * @since Sprint 5
     */

    private UUID lobbyID;
    private User currentUser;
    private Short cardID;
    private int counterCard;
    private Short costCard;

    public BuyCardMessage() {
    }

    public BuyCardMessage(UUID lobbyID, User currentUser, Short cardID, int counterCard, Short costCard) {
        this.lobbyID = lobbyID;
        this.currentUser = currentUser;
        this.cardID = cardID;
        this.counterCard = counterCard;
        this.costCard = costCard;
    }

    public UUID getLobbyID() {
        return lobbyID;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public Short getCardID() {
        return cardID;
    }

    public int getCounterCard() {
        return counterCard;
    }

    public Short getCostCard() {
        return costCard;
    }
}
