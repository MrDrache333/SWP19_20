package de.uol.swp.common.game.messages;

import de.uol.swp.common.game.response.AbstractGameMessage;
import de.uol.swp.common.user.User;

import java.util.UUID;

/**
 * Message, die gesendet wird, wenn ein Spieler eine Reaktionskarte aufdeckt.
 *
 * @author Julia
 * @since Sprint 10
 */
public class PlayedReactionCardMessage extends AbstractGameMessage {

    private static final long serialVersionUID = -7445487839454441681L;

    public PlayedReactionCardMessage(short cardID, User player, UUID gameID) {
        super(gameID, player, cardID);
    }
}
