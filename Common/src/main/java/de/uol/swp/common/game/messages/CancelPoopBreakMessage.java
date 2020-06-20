package de.uol.swp.common.game.messages;

import de.uol.swp.common.message.AbstractServerMessage;
import de.uol.swp.common.user.User;

import java.util.UUID;

public class CancelPoopBreakMessage extends AbstractServerMessage {

    private final UUID gameID;
    private final User user;

    public CancelPoopBreakMessage (UUID gameID, User user) {
        this.gameID = gameID;
        this.user = user;
    }

    public UUID getGameID() {
        return gameID;
    }

    public User getUser() {
        return user;
    }
}
