package de.uol.swp.common.game.card;

import de.uol.swp.common.game.card.parser.components.CardAction.CompositeCardAction;

import java.util.ArrayList;

/**
 * Die AKtionkarte
 */
public class ActionCard extends Card {

    private ArrayList<CompositeCardAction> actions;

    /**
     * Erstellt eine neue Aktionskarte
     *
     * @param name    Der name der Karte
     * @param id      Die ID der Karte
     * @param costs   the costs
     * @param actions the actions
     * @author KenoO
     * @since Sprint 5
     */
    public ActionCard(String name, short id, short costs, ArrayList<CompositeCardAction> actions) {
        super(Type.ActionCard, name, id, costs);
        this.actions = actions;
    }

    /**
     * Bestimmt den Aktionstyp einer Karte.
     */
    public enum ActionType {
        /**
         * Reaktionskarte.
         */
        Reaction,
        /**
         * Angriffskarte.
         */
        Attack,
        /**
         * Normale Aktionskarte.
         */
        Action
    }

    /**
     * Gibt die Kartenaktionen zurück.
     *
     * @return Die Kartenaktionen
     * @author KenoO
     * @since Sprint 6
     */
    public ArrayList<CompositeCardAction> getActions() {
        return actions;
    }
}
