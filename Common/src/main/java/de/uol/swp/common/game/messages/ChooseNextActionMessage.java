package de.uol.swp.common.game.messages;

import de.uol.swp.common.game.card.parser.components.CardAction.CardAction;
import de.uol.swp.common.message.AbstractServerMessage;

import java.util.List;
import java.util.UUID;

/**
 * Message, die gesendet wird, wenn sich der Spieler zwischen mehreren möglichen Aktionen entscheiden kann
 *
 * @author Julia
 * @since Sprint6
 */
public class ChooseNextActionMessage extends AbstractServerMessage {

    private static final long serialVersionUID = 860181339817068498L;
    private UUID gameID;
    private List<CardAction> actions;

    public ChooseNextActionMessage(UUID gameID, List<CardAction> actions) {
        this.gameID = gameID;
        this.actions = actions;
    }

    public UUID getGameID() {
        return gameID;
    }

    public List<CardAction> getActions() {
        return actions;
    }
}
