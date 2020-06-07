package de.uol.swp.common.game.messages;

import de.uol.swp.common.message.AbstractServerMessage;
import de.uol.swp.common.user.User;

import java.util.UUID;

public class BuyCardMessage extends AbstractServerMessage {

    private UUID gameID;
    private User currentUser;
    private Short cardID;
    private int counterCard;
    private Short costCard;

    public BuyCardMessage() {
    }

    /**
     * Die Antwort auf die BuyCardRequest
     *
     * @param gameID     die GameID
     * @param currentUser der User der die Request gestellt hat
     * @param cardID      die ID der Karte (String)
     * @param counterCard Anzahl der Karten (die die selbe ID haben) die man noch kaufen kann
     * @param costCard    die Kosten der zu kaufenden Karte
     * @author Rike
     * @since Sprint 5
     */
    public BuyCardMessage(UUID gameID, User currentUser, Short cardID, int counterCard, Short costCard) {
        this.gameID = gameID;
        this.currentUser = currentUser;
        this.cardID = cardID;
        this.counterCard = counterCard;
        this.costCard = costCard;
    }

    /**
     * Gibt die GameID zurück
     *
     * @return gameID die GameID
     * @author Rike, Darian
     * @since Sprint 3
     */
    public UUID getGameID() {
        return gameID;
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
