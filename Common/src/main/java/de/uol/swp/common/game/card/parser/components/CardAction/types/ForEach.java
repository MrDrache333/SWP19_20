package de.uol.swp.common.game.card.parser.components.CardAction.types;

import de.uol.swp.common.game.card.Card;
import de.uol.swp.common.game.card.parser.components.CardAction.CardAction;
import de.uol.swp.common.game.card.parser.components.CardAction.CompositeCardAction;

import java.util.ArrayList;

/**
 * Aktion, welche bestimmte Aktionen auf mehrere Karten anwenden kann
 */
public class ForEach extends CardAction {

    /**
     * Die Karten, auf die die Aktionen angewendet werden sollen
     */
    private ArrayList<Card> cards;
    /**
     * Die Aktionen, welche auf die Karten angewendet werden sollen
     */
    private ArrayList<CompositeCardAction> actions;

    /**
     * Erstellt eine neue Aktion
     *
     * @param cards   Die Karten
     * @param actions Die AKtionen
     * @author KenoO
     * @since Sprint 6
     */
    public ForEach(ArrayList<Card> cards, ArrayList<CompositeCardAction> actions) {
        this.cards = cards;
        this.actions = actions;
    }

    @Override
    public boolean execute() {
        return false;
    }
}
