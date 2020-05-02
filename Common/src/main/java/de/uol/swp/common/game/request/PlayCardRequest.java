package de.uol.swp.common.game.request;

import de.uol.swp.common.message.AbstractRequestMessage;
import de.uol.swp.common.user.User;

import java.util.UUID;

public class PlayCardRequest extends AbstractRequestMessage {

    private static final long serialVersionUID = -7995157255412993346L;
    /**
     * Die Request die gestellt wird, wenn eine Karte von der hand angeklickt wurde
     *
     * @param gameID       Die ID der aktuellen Lobby
     * @param user         Der SPieler, der eine Karte ausspielen möchte
     * @param id    Die ID der angeklickten Karte
     * @author Rike
     * @since Sprint 5
     */

    private UUID gameID;
    private User user;
    private Short id;

    public PlayCardRequest() {
    }

    public PlayCardRequest(UUID gameID, User currentUser, Short handCardID) {

        this.gameID = gameID;
        this.user = currentUser;
        this.id = handCardID;
    }

    public UUID getGameID() {
        return gameID;
    }

    public User getCurrentUser() {
        return user;
    }

    public Short getHandCardID() {
        return id;
    }
}
