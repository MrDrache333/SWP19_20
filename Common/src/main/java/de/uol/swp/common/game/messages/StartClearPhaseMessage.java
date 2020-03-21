package de.uol.swp.common.game.messages;

import de.uol.swp.common.message.AbstractServerMessage;
import de.uol.swp.common.user.User;

import java.util.UUID;

/**
 * Message die signalisiert, dass sich der User jetzt in der Clearphase befindet
 *
 * @author Julia
 * @since Sprint5
 */
public class StartClearPhaseMessage extends AbstractServerMessage {

    private static final long serialVersionUID = -4262409792917076822L;
    private User user;
    private UUID gameID;

    public StartClearPhaseMessage(User user, UUID gameID) {
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
