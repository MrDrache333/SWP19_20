package de.uol.swp.common.game.card.parser.components.CardAction.response;

import de.uol.swp.common.message.AbstractRequestMessage;
import de.uol.swp.common.user.User;

import java.util.UUID;

/**
 * Antwortnachricht auf ein OptionalActionRequest
 *
 * @author Julia
 * @since Sprint7
 */
public class OptionalActionResponse extends AbstractRequestMessage {

    private static final long serialVersionUID = -4612871995662134853L;
    private boolean execute;
    private UUID gameID;
    private User player;

    public OptionalActionResponse(UUID gameID, User player, boolean execute) {
        this.execute = execute;
        this.gameID = gameID;
        this.player = player;
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
}
