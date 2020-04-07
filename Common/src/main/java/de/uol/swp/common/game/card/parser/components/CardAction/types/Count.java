package de.uol.swp.common.game.card.parser.components.CardAction.types;

import de.uol.swp.common.game.card.Card;
import de.uol.swp.common.game.card.parser.components.CardAction.ComplexCardAction;

import java.util.ArrayList;

/**
 * Aktion, welche die Anzahl an Karten in einem Array zählt
 */
public class Count extends ComplexCardAction {

    /**
     * Die Karten, auf die die Aktionen angewendet werden sollen
     */
    private ArrayList<Card> cards;

    /**
     * Erstellt eine neue Aktion
     *
     * @param cards Die Karten
     * @author KenoO, Timo
     * @since Sprint 6
     */
    public Count(ArrayList<Card> cards) {
        this.cards = cards;
    }

    @Override
    public boolean execute() {
        return false;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }
}
