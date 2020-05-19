package de.uol.swp.common.game.messages;

import de.uol.swp.common.game.phase.Phase;
import de.uol.swp.common.message.AbstractServerMessage;
import de.uol.swp.common.user.User;

import java.util.UUID;

public class InfoPlayDisplayMessage extends AbstractServerMessage {

    /**
     * Aktualisiert die Anzeige, zur Wiedergabe der verfügbaren Käufen/Aktionen/Geld
     *
     * @param lobbyID           die LobbyID
     * @param currentUser       der User der die Request gestellt hat
     * @param availableAction   Anzahl der Aktionen, die ein User noch tätigen kann
     * @param availableBuy      Anzahl der Käufe, die ein User noch tätigen kann
     * @param additionalMoney   Zusätzliches Geld durch Aktionsn
     * @param moneyOnHand       das Geld, das der Spieler auf der Hand hat
     * @param sourceMessage     gibt Auskunft darüber in welcher Phase die jeweilige Message erstellt wurde
     * @param
     * @author Rike
     * @since Sprint 7
     */

    private UUID lobbyID;
    private User currentUser;
    private int availableAction;
    private int availableBuy;
    private int additionalMoney;
    private int moneyOnHand;
    private Phase.Type sourceMessage;

    public InfoPlayDisplayMessage() {

    }

    public InfoPlayDisplayMessage(UUID lobbyID, User currentUser, int availableAction, int availableBuy, int additionalMoney, int moneyOnHand, Phase.Type sourceMessage) {
        this.lobbyID = lobbyID;
        this.currentUser = currentUser;
        this.availableAction = availableAction;
        this.availableBuy = availableBuy;
        this.additionalMoney = additionalMoney;
        this.moneyOnHand = moneyOnHand;
        this.sourceMessage = sourceMessage;
    }

    public UUID getLobbyID() {
        return lobbyID;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public int getAvailableAction() {
        return availableAction;
    }

    public int getAvailableBuy() {
        return availableBuy;
    }

    public int getAdditionalMoney() {
        return additionalMoney;
    }

    public int getMoneyOnHand() {
        return moneyOnHand;
    }

    public Phase.Type getSourceMessage() {
        return sourceMessage;
    }
}
