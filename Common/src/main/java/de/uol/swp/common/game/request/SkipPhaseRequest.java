package de.uol.swp.common.game.request;

import de.uol.swp.common.message.AbstractRequestMessage;
import de.uol.swp.common.user.User;

import java.util.UUID;

/**
 * Request um die aktuelle Phase zu überspringen
 *
 * @author Julia
 * @aince Sprint5
 */
public class SkipPhaseRequest extends AbstractRequestMessage {

    private static final long serialVersionUID = -1742210033562469726L;
    private User user;
    private UUID gameID;
    private String phase;

    public SkipPhaseRequest(User user, UUID gameID, String phase) {
        this.user = user;
        this.gameID = gameID;
        this.phase = phase;
    }

    public User getUser() {
        return user;
    }

    public UUID getGameID() {
        return gameID;
    }
}
