package de.uol.swp.common.game.request;

import de.uol.swp.common.message.AbstractRequestMessage;
import de.uol.swp.common.user.User;

import java.util.UUID;

public class PlayCardRequest extends AbstractRequestMessage {

    private static final long serialVersionUID = -7995157255412993346L;
    /**
     * Die Request die gestellt wird, wenn eine Karte von der hand angeklickt wurde
     *
     * @param lobbyID       Die ID der aktuellen Lobby
     * @param currentUser   Der Spieler der die Request stellt
     * @param handCardID    Die ID der angeklickten Karte
     * @param cardImage     Die ImageView der Karte
     * @param handCards     Das Array mit den ImageViews die auf der Hand sind
     * @param smallSpace    gibt an, ob die Karten zusammen gerückt sind oder nicht
     * @author Rike
     * @since Sprint 5
     */

    private int count;
    private UUID gameID;
    private User user;
    private Short id;

    public PlayCardRequest() {
    }

    public PlayCardRequest(UUID gameID, User currentUser, Short handCardID, int count) {

        this.gameID = gameID;
        this.user = currentUser;
        this.id = handCardID;
        this.count = count;
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
