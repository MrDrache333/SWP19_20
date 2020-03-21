package de.uol.swp.common.game.card.parser.action.types;

import de.uol.swp.common.game.AbstractPlayground;
import de.uol.swp.common.game.card.Card;
import de.uol.swp.common.game.card.parser.action.ComplexCardAction;

import java.util.ArrayList;

public class Move extends ComplexCardAction {

    private ArrayList<Card> cardsToMove;    //Karten, die bewegt werden sollen.

    public Move(ArrayList<Card> cardsToMove, AbstractPlayground.ZoneType source, AbstractPlayground.ZoneType destination) {
        this.cardsToMove = cardsToMove;
        setCardSource(source);
        setCardDestination(destination);
    }

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
}
