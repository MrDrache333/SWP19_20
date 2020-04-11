package de.uol.swp.common.game.request;

import de.uol.swp.common.user.User;

import java.util.UUID;

public class PlayCardRequest {

    /**
     * Die Request die gestellt wird, wenn eine Karte von der hand angeklickt wurde
     *
     * @param lobbyID       Die ID der aktuellen Lobby
     * @param currentUser   Der Spieler der die Request stellt
     * @param handCardID    Die ID der angeklickten Karte
     * @param handCards     Das Array mit den ImageViews die auf der Hand sind
     * @param smallSpace    gibt an, ob die Karten zusammen gerückt sind oder nicht
     * @author Rike
     * @since Sprint 5
     */

    private UUID lobbyID;
    private User currentUser;
    private Short handCardID;

    public PlayCardRequest() {
    }

    public PlayCardRequest(UUID lobbyID, User currentUser, Short handCardID) {
        this.lobbyID = lobbyID;
        this.currentUser = currentUser;
        this.handCardID = handCardID;
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

}
