package de.uol.swp.common.game.messages;

import de.uol.swp.common.game.response.AbstractGameMessage;
import de.uol.swp.common.user.User;

import java.util.UUID;

public class RemoveActionCardMessage extends AbstractGameMessage {

    private static final long serialVersionUID = -864158329096483358L;

    public RemoveActionCardMessage(Short cardID, UUID gameID, User player) {
        super(gameID, player, cardID);
    }
}
