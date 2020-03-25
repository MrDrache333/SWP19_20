package de.uol.swp.common.game.messages;

import de.uol.swp.common.message.AbstractServerMessage;
import de.uol.swp.common.user.User;
import javafx.scene.image.ImageView;

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
     * @author Rike
     * @since Sprint 5
     */

    private UUID lobbyID;
    private User currentUser;
    private Short cardID;
    private ImageView cardImage;
    private boolean buyCard;
    private int counterCard;

    public BuyCardMessage() {
    }

    public BuyCardMessage (UUID lobbyID, User currentUser, Short cardID, ImageView cardImage, boolean buyCard, int counterCard){
        this.lobbyID = lobbyID;
        this.currentUser = currentUser;
        this.cardID = cardID;
        this.cardImage = cardImage;
        this.buyCard = buyCard;
        this.counterCard = counterCard;
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

    public ImageView getCardImage() {
        return cardImage;
    }

    public boolean isBuyCard() {
        return buyCard;
    }

    public int getCounterCard() {
        return counterCard;
    }
}
