package de.uol.swp.common.game.card;

import de.uol.swp.common.game.card.parser.components.CardAction.CompositeCardAction;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Die AKtionkarte
 */
public class ActionCard extends Card {

    /**
     * Die Kartenaktionen
     */
    private ArrayList<CompositeCardAction> actions;
    /**
     * Der Spezialisierungstyp
     */
    private ActionType type;

    /**
     * Erstellt eine neue Aktionskarte
     *
     * @param name    Der name der Karte
     * @param id      Die ID der Karte
     * @param costs   the costs
     * @param actions the actions
     * @param type    the type
     * @author KenoO
     * @since Sprint 5
     */
    public ActionCard(String name, short id, short costs, ArrayList<CompositeCardAction> actions, ActionType type) {
        super(Type.ActionCard, name, id, costs);
        this.type = Objects.requireNonNullElse(type, ActionType.Action);
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

    /**
     * Gibt den Spezialisierungstyp der Karte zurück
     *
     * @return Der Typ
     * @author KenoO
     * @since Sprint 6
     */
    public ActionType getType() {
        return type;
    }
}
