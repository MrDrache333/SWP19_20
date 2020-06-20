package de.uol.swp.common.game.request;

import de.uol.swp.common.message.AbstractRequestMessage;
import de.uol.swp.common.user.User;

import java.util.UUID;

public class CancelPoopBreakRequest extends AbstractRequestMessage {

    private final User user;
    private final UUID gameID;

    public CancelPoopBreakRequest(User user, UUID gameID) {
        this.user = user;
        this.gameID = gameID;
    }

    public User getUser() {
        return user;
    }

    public UUID getGameID() {
        return gameID;
    }
}
