package de.uol.swp.common.game.card.parser.components.CardAction.types;

import de.uol.swp.common.game.AbstractPlayground;
import de.uol.swp.common.game.card.Card;
import de.uol.swp.common.game.card.parser.components.CardAction.ComplexCardAction;

/**
 * Zeigt eine Karte einem selber bzw. allen Mitspielern von einer gewissen Quelle
 */
public class ShowCard extends ComplexCardAction {

    /**
     * Die Karte, die angezeigt werden soll
     */
    private Card card;

    /**
     * Erstellt eine neue Aktion
     *
     * @param card Die anzuzeigende Karte
     * @author KenoO
     * @since Sprint 6
     */
    public ShowCard(Card card, AbstractPlayground.ZoneType source) {
        this.card = card;
        this.setCardSource(source);
    }

    public Card getCard() {
        return card;
    }


    /**
     * Sets new Die Karte, die angezeigt werden soll.
     *
     * @param card New value of Die Karte, die angezeigt werden soll.
     */
    public void setCard(Card card) {
        this.card = card;
    }
}
