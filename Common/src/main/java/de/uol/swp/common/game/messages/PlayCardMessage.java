package de.uol.swp.common.game.messages;

import de.uol.swp.common.user.User;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.UUID;

public class PlayCardMessage {

    /**
     * Die Message die gesendet wird, wenn eine Handkarte angeklickt wurde
     *
     * @param lobbyID       Die ID der aktuellen Lobby
     * @param currentUser   Der Spieler der die Request stellt
     * @param handCardID    Die ID der angeklickten Karte
     * @param cardImage     Die ImageView der Karte
     * @param handCards     Das Array mit den ImageViews die auf der Hand sind
     * @param smallSpace    gibt an, ob die Karten zusammen gerückt sind oder nicht
     * @param count         gibt an, die wievielte Karte gespielt wurde
     * @param playCard      gibt an, ob die karte gespielt werden kann
     * @author Rike
     * @since Sprint 5
     */

    private UUID lobbyID;
    private User currentUser;
    private Short handCardID;
    private ImageView cardImage;
    private ArrayList<ImageView> handCards;
    private int count;
    private boolean playCard;

    public PlayCardMessage() {
    }

    public PlayCardMessage(UUID lobbyID, User currentUser, Short handCardID, ImageView cardImage,
                           ArrayList<ImageView> handCards, int count, boolean playCard) {
        this.lobbyID = lobbyID;
        this.currentUser = currentUser;
        this.handCardID = handCardID;
        this.cardImage = cardImage;
        this.handCards = handCards;
        this.count = count;
        this.playCard = playCard;
    }

    public UUID getLobbyID() {
        return lobbyID;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public Short getHandCardID() {
        return handCardID;
    }

    public ImageView getCardImage() {
        return cardImage;
    }

    public ArrayList<ImageView> getHandCards() {
        return handCards;
    }

    public int getCount() {
        return count;
    }

    public boolean isPlayCard() {
        return playCard;
    }
}
