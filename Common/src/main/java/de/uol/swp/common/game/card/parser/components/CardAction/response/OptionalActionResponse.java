package de.uol.swp.common.game.card.parser.components.CardAction.response;

import de.uol.swp.common.game.AbstractGameMessage;
import de.uol.swp.common.user.User;

import java.util.UUID;

/**
 * Antwortnachricht auf ein OptionalActionRequest
 *
 * @author Julia
 * @since Sprint7
 */
public class OptionalActionResponse extends AbstractGameMessage {

    private static final long serialVersionUID = -4612871995662134853L;
    private boolean execute;

    public OptionalActionResponse(UUID gameID, User player, boolean execute) {
        super(gameID, player);
        this.execute = execute;
    }

    public boolean isExecute() {
        return execute;
    }
}