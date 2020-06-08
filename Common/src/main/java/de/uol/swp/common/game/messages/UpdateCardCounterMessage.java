package de.uol.swp.common.game.messages;

import de.uol.swp.common.game.AbstractGameMessage;
import de.uol.swp.common.user.User;

import java.util.Map;
import java.util.UUID;

/**
 * Message, die gesendet wird, wenn sich die Anzahl verfügbarer Karten durch Ausspielen einer Aktionskarte verändert hat
 *
 * @author Julia
 * @since Sprint 8
 */
public class UpdateCardCounterMessage extends AbstractGameMessage {

    private static final long serialVersionUID = 710636197251694523L;
    private final Map<Short, Integer> cardCounts;

    public UpdateCardCounterMessage(UUID gameID, User player, Map<Short, Integer> cardCounts) {
        super(gameID, player);
        this.cardCounts = cardCounts;
    }

    public Map<Short, Integer> getCardCounts() {
        return cardCounts;
    }
}
