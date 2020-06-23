package de.uol.swp.common.game.card.parser.components.CardAction.response;

import de.uol.swp.common.message.AbstractRequestMessage;
import de.uol.swp.common.user.User;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Die Antwortnachricht auf ein ChooseCardRequest
 */
public class ChooseCardResponse extends AbstractRequestMessage {

    private static final long serialVersionUID = -3212704229461733936L;

    /**
     * Die ausgewählten Karten
     */
    private final ArrayList<Short> cards;
    private final UUID gameID;
    private final User player;
    private final int actionExecutionID;

    /**
     * Erstellt eine neue Antwortnachricht
     *
     * @param gameID            Die ID des Games
     * @param player            Der Spieler
     * @param choosenCard       Die ausgewählte Karte
     * @param actionExecutionID Die ID der ActionCardExecution
     * @author KenoO
     * @since Sprint 7
     */
    public ChooseCardResponse(UUID gameID, User player, short choosenCard, int actionExecutionID) {
        this.gameID = gameID;
        this.player = player;
        cards = new ArrayList<>();
        cards.add(choosenCard);
        this.actionExecutionID = actionExecutionID;
    }

    /**
     * Erstellt eine neue Antwortnachricht
     *
     * @param gameID            Die ID des Games
     * @param player            Der Spieler
     * @param choosenCards      Die ausgewählten Karten
     * @param actionExecutionID Die ID der ActionCardExecution
     * @author KenoO
     * @since Sprint 7
     */
    public ChooseCardResponse(UUID gameID, User player, ArrayList<Short> choosenCards, int actionExecutionID) {
        this.gameID = gameID;
        this.player = player;
        cards = choosenCards;
        this.actionExecutionID = actionExecutionID;
    }

    /**
     * Gets cards.
     *
     * @return the cards
     * @author KenoO
     * @since Sprint 7
     */
    public ArrayList<Short> getCards() {
        return cards;
    }


    public UUID getGameID() {
        return gameID;
    }

    public User getPlayer() {
        return player;
    }

    public int getActionExecutionID() {
        return actionExecutionID;
    }
}

