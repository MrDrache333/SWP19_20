package de.uol.swp.common.game.card.parser.components.CardAction.types;

import de.uol.swp.common.game.card.parser.components.CardAction.CardAction;
import de.uol.swp.common.game.card.parser.components.CardAction.SimpleCardAction;
import de.uol.swp.common.game.card.parser.components.CardAction.types.subtypes.Condition;

/**
 * Die IF-Aktion (Bedingte ausführung einer weiteren Aktion).
 */
public class If extends SimpleCardAction {

    /**
     * Die Aktion, deren ergebnis ausgewertet wird
     */
    private Condition condition;
    /**
     * Gewünschtes Ergebnis der vorherigen Aktion
     */
    private boolean expectedResult = true;
    /**
     * Die Aktion, deren Ausführung von dem Ergebnis der vorhergegangen Aktion abhängt
     */
    private CardAction conditionedAction;

    /**
     * Erstellt eine neue If-Aktion
     *
     * @param condition         Die Aktion, deren ergebnis ausgewertet wird
     * @param expectedResult    Gewünschtes Ergebnis der vorherigen Aktion
     * @param conditionedAction Die Aktion, deren ausführung von dem ergebnis der vorhergegangen Aktion abhängt
     * @author KenoO
     * @since Sprint 6
     */
    public If(Condition condition, boolean expectedResult, CardAction conditionedAction) {
        this.condition = condition;
        this.expectedResult = expectedResult;
        this.conditionedAction = conditionedAction;
    }

    @Override
    public boolean execute() {
        //TODO
        return false;
    }
}
