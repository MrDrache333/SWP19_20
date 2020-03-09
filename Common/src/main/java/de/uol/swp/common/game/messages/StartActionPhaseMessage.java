package de.uol.swp.common.game.messages;

import de.uol.swp.common.message.AbstractServerMessage;
import de.uol.swp.common.user.User;

import java.util.UUID;

/**
 * Message die signalisiert, dass der User mit der Aktionsphase starten kann
 *
 * @author Julia
 * @since Sprint5
 */
public class StartActionPhaseMessage extends AbstractServerMessage {

    private static final long serialVersionUID = -7186103893383553989L;
    private User user;
    private UUID gameID;

    public StartActionPhaseMessage(User user, UUID gameID) {
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
