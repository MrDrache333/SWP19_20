package de.uol.swp.common.game.card.parser.components.CardAction.types;

import de.uol.swp.common.game.card.parser.components.CardAction.ComplexCardAction;
import de.uol.swp.common.game.card.parser.components.CardAction.CompositeCardAction;
import de.uol.swp.common.game.card.parser.components.CardAction.types.subtypes.Condition;

import java.util.ArrayList;

/**
 * Aktion, welche andere Aktionen ausführt solange eine Bedingung gültig ist
 */
public class While extends ComplexCardAction {

    /**
     * Die Bedingung
     */
    private Condition condition;
    /**
     * Die auszuführenden Aktionen
     */
    private ArrayList<CompositeCardAction> actions;

    /**
     * Erstellt eine neue Aktion
     *
     * @param condition Die Bedingung
     * @param actions   Die auszuführenden Aktionen
     * @author KenoO
     * @since Sprint 6
     */
    public While(Condition condition, ArrayList<CompositeCardAction> actions) {
        this.condition = condition;
        this.actions = actions;
    }

    /**
     * Gibt die Bedingung wieder zurück
     *
     * @return Die Bedingung
     * @author KenoO
     * @since Sprint 6
     */
    public Condition getCondition() {
        return condition;
    }

    /**
     * Gibt die AKtionen wieder zurück
     *
     * @return Die Aktionen
     * @author KenoO
     * @since Sprint 6
     */
    public ArrayList<CompositeCardAction> getActions() {
        return actions;
    }

    @Override
    public boolean execute() {
        return false;
    }
}
