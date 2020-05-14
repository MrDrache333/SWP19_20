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
     * @param buyCard       gibt an, ob der Kauf erfolgreich war
     * @param counterCard   Anzahl der Karten (die die selbe ID haben) die man noch kaufen kann
     * @param moneyValuePlayer  Geld das der User nach dem Kauf noch auf der Hand hat
     * @param availableBuys    Anzahl der Käufe, die der User noch tätigen kann
     * @param additionalMoney  Wert des zusätzlichen Geldes (durch Aktionskarten)
     * @author Rike
     * @since Sprint 5
     */

    private UUID lobbyID;
    private User currentUser;
    private Short cardID;
    private boolean buyCard;
    private int counterCard;
    private int moneyValuePlayer;
    private int availableBuys;
    private int additionalMoney;

    public BuyCardMessage() {
    }

    public BuyCardMessage(UUID lobbyID, User currentUser, Short cardID, boolean buyCard, int counterCard, int moneyValuePlayer, int additionalMoney, int availableBuys) {
        this.lobbyID = lobbyID;
        this.currentUser = currentUser;
        this.cardID = cardID;
        this.buyCard = buyCard;
        this.counterCard = counterCard;
        this.moneyValuePlayer = moneyValuePlayer;
        this.additionalMoney = additionalMoney;
        this.availableBuys = availableBuys;
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

    public boolean isBuyCard() {
        return buyCard;
    }

    public int getCounterCard() {
        return counterCard;
    }

    public int getMoneyValuePlayer() {
        return moneyValuePlayer;
    }

    public int getAdditionalMoney() {
        return additionalMoney;
    }

    public int getAvailableBuys() {
        return availableBuys;
    }
}
