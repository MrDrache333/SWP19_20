package de.uol.swp.common.game.request;

import de.uol.swp.common.message.AbstractRequestMessage;
import de.uol.swp.common.user.User;

import java.util.UUID;

public class PlayCardRequest extends AbstractRequestMessage {

    private static final long serialVersionUID = -7995157255412993346L;

    private UUID gameID;
    private User user;
    private Short id;

    /**
     * Basiskonstruktor der PlayCardRequest
     *
     * @author Rike
     * @since Sprint 5
     */
    public PlayCardRequest() {
    }

    /**
     * Die Request die gestellt wird, wenn eine Karte von der hand angeklickt wurde
     *
     * @param gameID      Die ID der aktuellen Lobby
     * @param currentUser Der Spieler, der eine Karte ausspielen möchte
     * @param handCardID  Die ID der angeklickten Karte
     * @author Rike
     * @since Sprint 5
     */
    public PlayCardRequest(UUID gameID, User currentUser, Short handCardID) {

        this.gameID = gameID;
        this.user = currentUser;
        this.id = handCardID;
    }

    /**
     * Gibt die Game-ID zurück
     *
     * @return gameID die Game-ID
     * @author Rike, Devin
     * @since Sprint 6
     */
    public UUID getGameID() {
        return gameID;
    }

    /**
     * Gibt die Game-ID zurück
     *
     * @return user der aktuelle User
     * @author Rike, Devin
     * @since Sprint 6
     */
    public User getCurrentUser() {
        return user;
    }

    /**
     * Gibt die ID der angeklickten Karte zurück
     *
     * @return id die ID der angeklickten Karte
     * @author Rike, Devin
     * @since Sprint 6
     */
    public Short getHandCardID() {
        return id;
    }
}
