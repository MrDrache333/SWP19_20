package de.uol.swp.common.game.messages;

import de.uol.swp.common.message.AbstractServerMessage;
import de.uol.swp.common.user.User;

import java.util.UUID;

public class BuyCardMessage extends AbstractServerMessage {

    private UUID lobbyID;
    private User currentUser;
    private Short cardID;
    private int counterCard;
    private Short costCard;

    public BuyCardMessage() {
    }

    /**
     * Die Antwort auf die BuyCardRequest
     *
     * @param lobbyID     Die LobbyID
     * @param currentUser Der User der die Request gestellt hat
     * @param cardID      Die ID der Karte (String)
     * @param counterCard Anzahl der Karten (die die selbe ID haben) die man noch kaufen kann
     * @param costCard    Die Kosten der zu kaufenden Karte
     * @author Rike
     * @since Sprint 5
     */
    public BuyCardMessage(UUID lobbyID, User currentUser, Short cardID, int counterCard, Short costCard) {
        this.lobbyID = lobbyID;
        this.currentUser = currentUser;
        this.cardID = cardID;
        this.counterCard = counterCard;
        this.costCard = costCard;
    }

    /**
     * Gibt die LobbyID zurück
     *
     * @return lobbyID die LobbyID
     * @author Rike
     * @since Sprint 3
     */
    public UUID getLobbyID() {
        return lobbyID;
    }

    /**
     * Gibt den derzeitigen User zurück
     *
     * @return currentUser der aktuelle User
     * @author Rike
     * @since Sprint 3
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Gibt die Karten-ID zurück
     *
     * @return cardID die Karten-ID
     * @author Rike
     * @since Sprint 3
     */
    public Short getCardID() {
        return cardID;
    }

    /**
     * Gibt die Anzahl der verfügbaren Kartenkäufe zurück
     *
     * @return counterCard die Anzahl der verfügbaren Kartenkäufe
     * @author Rike
     * @since Sprint 3
     */
    public int getCounterCard() {
        return counterCard;
    }

    /**
     * Gibt den Kartenpreis zurück
     *
     * @return costCard der Kartenpreis
     * @author Rike
     * @since Sprint 3
     */
    public Short getCostCard() {
        return costCard;
    }
}
