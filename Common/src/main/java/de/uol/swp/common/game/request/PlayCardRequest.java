package de.uol.swp.common.game.request;

import de.uol.swp.common.message.AbstractRequestMessage;
import de.uol.swp.common.user.User;

import java.util.UUID;

public class PlayCardRequest extends AbstractRequestMessage {

    /**
     * Die Request die gestellt wird, wenn eine Karte von der hand angeklickt wurde
     *
     * @param count       Der Platz der Karte in der Hand, die gewählt wurde.
     * @author Rike , Devin
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

    public int getCount() {
        return count;
    }
}
