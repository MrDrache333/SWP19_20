package de.uol.swp.common.game.messages;

import de.uol.swp.common.game.AbstractGameMessage;
import de.uol.swp.common.game.card.parser.components.CardAction.types.Move;
import de.uol.swp.common.user.User;

import java.util.UUID;

/**
 * Enhält Informationen zu verschobenen Karten auf dem Spielfeld.
 */
public class MoveCardMessage extends AbstractGameMessage {

    /**
     * Die ausgeführte Kartenbewegung
     */
    private final Move move;

    /**
     * Instanziiert eine MoveCardMessage
     *
     * @param gameID Die ID des Games
     * @param player Der Spieler
     * @author Paula, KenoO
     * @since Sprint 7
     */
    public MoveCardMessage(UUID gameID, User player, Move move) {
        super(gameID, player);
        this.move = move;
    }

    public Move getMove() {
        return move;
    }
}
