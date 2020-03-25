package de.uol.swp.common.game.request;


import de.uol.swp.common.message.AbstractRequestMessage;
import de.uol.swp.common.user.User;

import java.util.UUID;

public class BuyCardRequest extends AbstractRequestMessage {

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
    private static final long serialVersionUID = -2002420509720238588L;
    private UUID lobbyID;
    private User currentUser;
    private Short cardID;


    public BuyCardRequest(UUID lobbyID, User currentUser, Short cardID) {
        this.lobbyID = lobbyID;
        this.currentUser = currentUser;
        this.cardID = cardID;
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
}
