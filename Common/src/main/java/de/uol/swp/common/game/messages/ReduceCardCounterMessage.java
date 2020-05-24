package de.uol.swp.common.game.messages;

import de.uol.swp.common.game.AbstractGameMessage;
import de.uol.swp.common.user.User;

import java.util.Map;
import java.util.UUID;

public class ReduceCardCounterMessage extends AbstractGameMessage {

    private static final long serialVersionUID = 710636197251694523L;
    Map<Short, Integer> cardCounts;

    public ReduceCardCounterMessage(UUID gameID, User player, Map<Short, Integer> cardCounts) {
        super(gameID, player);
        this.cardCounts = cardCounts;
    }

    public Map<Short, Integer> getCardCounts() {
        return cardCounts;
    }
}
