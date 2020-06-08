package de.uol.swp.common.game.request;

import de.uol.swp.common.message.AbstractRequestMessage;
import de.uol.swp.common.user.User;

import java.util.UUID;

/**
 * Request um die aktuelle Phase zu überspringen
 *
 * @author Julia
 * @since Sprint 5
 */
public class SkipPhaseRequest extends AbstractRequestMessage {

    private static final long serialVersionUID = -1742210033562469726L;
    private User user;
    private UUID gameID;

    /**
     * Konstruktor der SkipPhaseRequest
     *
     * @param user   der User
     * @param gameID die Game-ID
     * @author Julia
     * @since Sprint 5
     */
    public SkipPhaseRequest(User user, UUID gameID) {
        this.user = user;
        this.gameID = gameID;
    }

    /**
     * Gibt den User zurück
     *
     * @return user der User
     * @author Julia
     * @since Sprint 5
     */
    public User getUser() {
        return user;
    }

    /**
     * Gibt die Game-ID zurück
     *
     * @return gameID die Game-ID
     * @author Julia
     * @since Sprint 5
     */
    public UUID getGameID() {
        return gameID;
    }
}
