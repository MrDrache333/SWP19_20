package de.uol.swp.common.game.card.parser.components.CardAction.types;

import de.uol.swp.common.game.AbstractPlayground;
import de.uol.swp.common.game.card.Card;
import de.uol.swp.common.game.card.parser.components.CardAction.ComplexCardAction;

import java.util.ArrayList;

/**
 * Aktion, welche die angegebenen Karten von einer Quelle an das angegebene Ziel bewegt
 */
public class Move extends ComplexCardAction {

    /**
     * Die zu bewegenden Karten
     */
    private ArrayList<Card> cardsToMove;

    /**
     * Erstellt eine neue Aktion
     *
     * @param cardsToMove Die zu bewegenden Karten
     * @param source      Die Quelle
     * @param destination Das ziel
     * @author KenoO
     * @since Sprint
     */
    public Move(ArrayList<Card> cardsToMove, AbstractPlayground.ZoneType source, AbstractPlayground.ZoneType destination) {
        this.cardsToMove = cardsToMove;
        setCardSource(source);
        setCardDestination(destination);
    }

    /**
     * Erstellt eine neue Aktion
     *
     * @param cardToMove  Die zu bewegende Karte
     * @param source      Die Quelle
     * @param destination Das ziel
     * @author KenoO
     * @since Sprint
     */
    public Move(Card cardToMove, AbstractPlayground.ZoneType source, AbstractPlayground.ZoneType destination) {
        this.cardsToMove = new ArrayList<>();
        cardsToMove.add(cardToMove);
        setCardSource(source);
        setCardDestination(destination);
    }

    @Override
    public boolean execute() {
        return false;
    }

    public ArrayList<Card> getCardsToMove() {
        return cardsToMove;
    }
}
