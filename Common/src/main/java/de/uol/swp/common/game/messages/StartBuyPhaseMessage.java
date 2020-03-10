package de.uol.swp.common.game.messages;

import de.uol.swp.common.message.AbstractServerMessage;
import de.uol.swp.common.user.User;

import java.util.UUID;

/**
 * Message die signalisiert, dass der User mit der BuyPhase starten kann
 *
 * @author Julia
 * @since Sprint5
 */
public class StartBuyPhaseMessage extends AbstractServerMessage {

    private static final long serialVersionUID = 7590065068489875942L;
    private User user;
    private UUID gameID;

    public StartBuyPhaseMessage(User user, UUID gameID) {
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
