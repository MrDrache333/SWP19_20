package de.uol.swp.common.game.card.parser.components.CardAction.response;

import de.uol.swp.common.game.AbstractGameMessage;
import de.uol.swp.common.game.card.parser.components.CardAction.CardAction;
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
    private CardAction action;
    private boolean execute;
    private boolean subAction;

    public OptionalActionResponse(UUID gameID, User player, CardAction action, boolean execute, boolean subAction) {
        super(gameID, player);
        this.action = action;
        this.execute = execute;
        this.subAction = subAction;
    }

    public CardAction getAction() {
        return action;
    }

    public boolean isExecute() {
        return execute;
    }

    public boolean isSubAction() {
        return subAction;
    }
}
