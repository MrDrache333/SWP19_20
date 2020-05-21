package de.uol.swp.common.game.card.parser.components.CardAction.types;

import de.uol.swp.common.game.AbstractPlayground;
import de.uol.swp.common.game.card.Card;
import de.uol.swp.common.game.card.parser.components.CardAction.ComplexCardAction;

import java.util.ArrayList;

/**
 * Aktion um eine bestimmte Anzahl an Karten von einer Quelle zu holen.
 */
public class GetCard extends ComplexCardAction {

    /**
     * Die Anzahl der Karten (default=1)
     */
    private short count = 1;
    /**
     * Die "geholten" Karten aus der angegebenen Quelle
     */
    private ArrayList<Card> cards;

    /**
     * Erstellt eine neue Aktion
     *
     * @param count  Die Anzahl an Karten
     * @param source Die Quelle
     * @author KenoO
     * @since Sprint 6
     */
    public GetCard(short count, AbstractPlayground.ZoneType source) {
        this.count = count;
        setCardSource(source);
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    /**
     * Gibt die "geholten" Karten zurück
     *
     * @return Die "geholten" Karten
     * @author KenoO
     * @since Sprint 6
     */
    public ArrayList<Card> getCards() {
        return cards;
    }

    public short getCount() {
        return count;
    }


    /**
     * Sets new Die Anzahl der Karten default=1.
     *
     * @param count New value of Die Anzahl der Karten default=1.
     */
    public void setCount(short count) {
        this.count = count;
    }

    /**
     * Sets new Die "geholten" Karten aus der angegebenen Quelle.
     *
     * @param cards New value of Die "geholten" Karten aus der angegebenen Quelle.
     */
    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }
}
