package de.uol.swp.common.game.card.parser.components.CardAction.types;

import de.uol.swp.common.game.card.Card;
import de.uol.swp.common.game.card.parser.components.CardAction.CardAction;
import de.uol.swp.common.game.card.parser.components.CardAction.SimpleCardAction;

import java.util.ArrayList;

/**
 * Aktion, welche bestimmte Aktionen auf mehrere Karten anwenden kann
 */
public class ForEach extends SimpleCardAction {

    /**
     * Die Karten, auf die die Aktionen angewendet werden sollen
     */
    private ArrayList<Card> cards;
    /**
     * Die Aktionen, welche auf die Karten angewendet werden sollen
     */
    private ArrayList<CardAction> actions;

    /**
     * Erstellt eine neue Aktion
     *
     * @param cards   Die Karten
     * @param actions Die AKtionen
     * @author KenoO
     * @since Sprint 6
     */
    public ForEach(ArrayList<Card> cards, ArrayList<CardAction> actions) {
        this.cards = cards;
        this.actions = actions;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public ArrayList<CardAction> getActions() {
        return actions;
    }


    /**
     * Sets new Die Karten, auf die die Aktionen angewendet werden sollen.
     *
     * @param cards New value of Die Karten, auf die die Aktionen angewendet werden sollen.
     */
    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    /**
     * Sets new Die Aktionen, welche auf die Karten angewendet werden sollen.
     *
     * @param actions New value of Die Aktionen, welche auf die Karten angewendet werden sollen.
     */
    public void setActions(ArrayList<CardAction> actions) {
        this.actions = actions;
    }
}
