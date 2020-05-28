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

    /**
     * Konstruktor der StartBuyPhaseMessage
     *
     * @param user   der User
     * @param gameID die Game-ID
     * @author Julia
     * @since Sprint 6
     */
    public StartBuyPhaseMessage(User user, UUID gameID) {
        this.user = user;
        this.gameID = gameID;
    }

    /**
     * Gibt den User zurück
     *
     * @return user der User
     * @author Julia
     * @since Sprint 6
     */
    public User getUser() {
        return user;
    }

    /**
     * Gibt die Game-ID zurück
     *
     * @return gameID die Game-ID
     * @author Julia
     * @since Sprint 6
     */
    public UUID getGameID() {
        return gameID;
    }
}
