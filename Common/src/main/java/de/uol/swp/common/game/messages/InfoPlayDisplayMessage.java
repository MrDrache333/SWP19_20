package de.uol.swp.common.game.messages;

import de.uol.swp.common.game.phase.Phase;
import de.uol.swp.common.message.AbstractServerMessage;
import de.uol.swp.common.user.User;

import java.util.UUID;

public class InfoPlayDisplayMessage extends AbstractServerMessage {

    private UUID lobbyID;
    private User currentUser;
    private int availableAction;
    private int availableBuy;
    private int additionalMoney;
    private int moneyOnHand;
    private Phase.Type sourceMessage;

    public InfoPlayDisplayMessage() {

    }

    /**
     * Aktualisiert die Anzeige, zur Wiedergabe der verfügbaren Käufen/Aktionen/Geld
     *
     * @param lobbyID         die LobbyID
     * @param currentUser     der User der die Request gestellt hat
     * @param availableAction Anzahl der Aktionen, die ein User noch tätigen kann
     * @param availableBuy    Anzahl der Käufe, die ein User noch tätigen kann
     * @param additionalMoney Zusätzliches Geld durch Aktionsn
     * @param moneyOnHand     das Geld, das der Spieler auf der Hand hat
     * @param sourceMessage   gibt Auskunft darüber in welcher Phase die jeweilige Message erstellt wurde
     * @author Rike
     * @since Sprint 7
     */
    public InfoPlayDisplayMessage(UUID lobbyID, User currentUser, int availableAction, int availableBuy, int additionalMoney, int moneyOnHand, Phase.Type sourceMessage) {
        this.lobbyID = lobbyID;
        this.currentUser = currentUser;
        this.availableAction = availableAction;
        this.availableBuy = availableBuy;
        this.additionalMoney = additionalMoney;
        this.moneyOnHand = moneyOnHand;
        this.sourceMessage = sourceMessage;
    }

    /**
     * Gibt die LobbyID zurück
     *
     * @return lobbyID die Lobby ID
     * @author Rike
     * @since Sprint 7
     */
    public UUID getLobbyID() {
        return lobbyID;
    }

    /**
     * Gibt die den aktuellen Nutzer zurück
     *
     * @return currentUser den aktuellen User
     * @author Rike
     * @since Sprint 7
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Gibt die Anzahl der verfügbaren Aktionen zurück
     *
     * @return availableAction die Anzahl der verfügbaren Aktionen
     * @author Rike
     * @since Sprint 7
     */
    public int getAvailableAction() {
        return availableAction;
    }

    /**
     * Gibt die Anzahl der verfübaren Käufe zurück
     *
     * @return availableBuy die Anzahl der verfügbaren Käufe
     * @author Rike
     * @since Sprint 7
     */
    public int getAvailableBuy() {
        return availableBuy;
    }

    /**
     * Gibt das zusätzliche Geld zurück
     *
     * @return additonalMoney das zusätzliche Geld
     * @author Rike
     * @since Sprint 7
     */
    public int getAdditionalMoney() {
        return additionalMoney;
    }

    /**
     * Gibt die Geldkarten auf der Hand zurück
     *
     * @return moneyOnHand die Geldkarten auf der Hand
     * @author Rike
     * @since Sprint 7
     */
    public int getMoneyOnHand() {
        return moneyOnHand;
    }

    /**
     * Gibt die den Phasentyp zurück
     *
     * @return sourceMessage der Phasentyp
     * @author Rike
     * @since Sprint 7
     */
    public Phase.Type getSourceMessage() {
        return sourceMessage;
    }
}
