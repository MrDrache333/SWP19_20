package de.uol.swp.common.game.card.parser.components.CardAction.request;

import de.uol.swp.common.game.AbstractGameMessage;
import de.uol.swp.common.user.User;

import java.util.UUID;

/**
 * Request, das gesendet wird, wenn ein Spieler entscheiden kann, ob er eine Aktion ausführen will oder nicht.
 *
 * @author Julia
 * @since Sprint 7
 */
public class OptionalActionRequest extends AbstractGameMessage {

    private static final long serialVersionUID = -2721566171106710582L;
    private String textMessage;

    public OptionalActionRequest(UUID gameID, User player, String message) {
        super(gameID, player);
        this.textMessage = message;
    }

    public String getTextMessage() {
        return textMessage;
    }
}
