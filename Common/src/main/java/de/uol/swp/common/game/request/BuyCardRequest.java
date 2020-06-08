package de.uol.swp.common.game.request;


import de.uol.swp.common.message.AbstractRequestMessage;
import de.uol.swp.common.user.User;

import java.util.UUID;

public class BuyCardRequest extends AbstractRequestMessage {


    private static final long serialVersionUID = -2002420509720238588L;
    private UUID lobbyID;
    private User currentUser;
    private Short cardID;

    /**
     * Die Request die gestellt wird, wenn ein User eine Karte kaufen möchte
     *
     * @param lobbyID     die LobbyID
     * @param currentUser der User der die Request stellt
     * @param cardID      die ID der Karte
     * @author Rike
     * @since Sprint 6
     */
    public BuyCardRequest(UUID lobbyID, User currentUser, Short cardID) {
        this.lobbyID = lobbyID;
        this.currentUser = currentUser;
        this.cardID = cardID;
    }

    /**
     * Gibt die Lobby-ID zurück
     *
     * @return lobbyID die Lobby-ID
     * @author Rike
     * @since Sprint 6
     */
    public UUID getLobbyID() {
        return lobbyID;
    }

    /**
     * Gibt den aktuellen User zurück
     *
     * @return currentUser den aktuellen User
     * @author Rike
     * @since Sprint 6
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Gibt den die Karten-ID zurück
     *
     * @return cardID die Karten-ID
     * @author Rike
     * @since Sprint 6
     */
    public Short getCardID() {
        return cardID;
    }
}
