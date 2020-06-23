package de.uol.swp.common.game.card.parser.components.CardAction.response;

import de.uol.swp.common.message.AbstractRequestMessage;
import de.uol.swp.common.user.User;

import java.util.UUID;

/**
 * Antwortnachricht auf ein OptionalActionRequest
 *
 * @author Julia
 * @since Sprint 7
 */
public class OptionalActionResponse extends AbstractRequestMessage {

    private static final long serialVersionUID = -4612871995662134853L;
    private final boolean execute;
    private final UUID gameID;
    private final User player;
    private final int actionExecutionID;

    public OptionalActionResponse(UUID gameID, User player, boolean execute, int actionExecutionID) {
        this.execute = execute;
        this.gameID = gameID;
        this.player = player;
        this.actionExecutionID = actionExecutionID;
    }

    public boolean isExecute() {
        return execute;
    }

    public UUID getGameID() {
        return gameID;
    }

    public User getPlayer() {
        return player;
    }

    public int getActionExecutionID() {
        return actionExecutionID;
    }
}
