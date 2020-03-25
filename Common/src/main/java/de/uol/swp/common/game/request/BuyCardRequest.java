package de.uol.swp.common.game.request;


import de.uol.swp.common.user.User;
import javafx.scene.image.ImageView;

import java.util.UUID;

public class BuyCardRequest {

    /**
     * Die Request die gestellt wird, wenn ein User eine Karte kaufen möchte
     *
     * @param lobbyID       die LobbyID
     * @param currentUser   der User der die Request stellt
     * @param cardID        die ID der Karte (String)
     * @param cardImage     die ImageView der Karte
     * @author Rike
     * @since Sprint 5
     */

    private UUID lobbyID;
    private User currentUser;
    private Short cardID;
    private ImageView cardImage;

    public BuyCardRequest() {
    }

    public BuyCardRequest (UUID lobbyID, User currentUser, Short cardID, ImageView cardImage){
        this.lobbyID = lobbyID;
        this.currentUser = currentUser;
        this.cardID = cardID;
        this.cardImage = cardImage;
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
}
