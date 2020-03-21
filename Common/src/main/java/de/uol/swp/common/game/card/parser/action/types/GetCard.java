package de.uol.swp.common.game.card.parser.action.types;

import de.uol.swp.common.game.AbstractPlayground;
import de.uol.swp.common.game.card.Card;
import de.uol.swp.common.game.card.parser.action.ComplexCardAction;

import java.util.ArrayList;

/**
 * Aktion um eine bestimmte Anzahl an Karten von einer Quelle zu holen.
 */
public class GetCard extends ComplexCardAction {

    /**
     * The Count.
     */
    private short count;    //Anzahl der Karten
    private ArrayList<Card> cards;  //Geholte karten

    /**
     * Instantiates a new Get card.
     *
     * @param count the count
     * @author KenoO
     * @since Sprint 6
     */
    public GetCard(short count, AbstractPlayground.ZoneType source) {
        this.count = count;
        setCardSource(source);
    }

    @Override
    public boolean execute() {
        return false;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }
}
