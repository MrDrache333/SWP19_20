package de.uol.swp.common.game.card.parser.components.CardAction.response;

import de.uol.swp.common.game.AbstractGameMessage;
import de.uol.swp.common.game.AbstractPlayground;
import de.uol.swp.common.user.User;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Die Antwortnachricht auf ein ChooseCardRequest
 */
public class ChooseCardResponse extends AbstractGameMessage {

    private static final long serialVersionUID = -3212704229461733936L;

    /**
     * Die ausgewählten Karten
     */
    private ArrayList<Short> cards;

    private AbstractPlayground.ZoneType from;
    private boolean directHand;

    /**
     * Erstellt eine neue Antwortnachricht
     *
     * @param gameID      Die ID des Games
     * @param player      Der Spieler
     * @param choosenCard Die ausgewählte Karte
     * @author KenoO
     * @since Sprint7
     */
    public ChooseCardResponse(UUID gameID, User player, short choosenCard, boolean directHand) {
        super(gameID, player);
        cards = new ArrayList<>();
        cards.add(choosenCard);
        this.directHand = directHand;
    }

    /**
     * Erstellt eine neue Antwortnachricht
     *
     * @param gameID       Die ID des Games
     * @param player       Der Spieler
     * @param choosenCards Die ausgewählten Karten
     * @author KenoO
     * @since Sprint7
     */
    public ChooseCardResponse(UUID gameID, User player, ArrayList<Short> choosenCards, boolean directHand) {
        super(gameID, player);
        cards = choosenCards;
        this.directHand = directHand;
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

    public AbstractPlayground.ZoneType getFrom() {
        return from;
    }

    public boolean getDirectHand() {
        return directHand;
    }
}
