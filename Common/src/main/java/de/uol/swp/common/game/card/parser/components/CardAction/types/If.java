package de.uol.swp.common.game.card.parser.components.CardAction.types;

import de.uol.swp.common.game.card.parser.components.CardAction.CardAction;
import de.uol.swp.common.game.card.parser.components.CardAction.ComplexCardAction;
import de.uol.swp.common.game.card.parser.components.CardAction.types.subtypes.Condition;

/**
 * Die IF-Aktion (Bedingte ausführung einer weiteren Aktion).
 */
public class If extends ComplexCardAction {

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

    public Condition getCondition() {
        return condition;
    }

    public boolean isExpectedResult() {
        return expectedResult;
    }

    public CardAction getConditionedAction() {
        return conditionedAction;
    }


    /**
     * Sets new Die Aktion, deren ergebnis ausgewertet wird.
     *
     * @param condition New value of Die Aktion, deren ergebnis ausgewertet wird.
     */
    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    /**
     * Sets new Die Aktion, deren Ausführung von dem Ergebnis der vorhergegangen Aktion abhängt.
     *
     * @param conditionedAction New value of Die Aktion, deren Ausführung von dem Ergebnis der vorhergegangen Aktion abhängt.
     */
    public void setConditionedAction(CardAction conditionedAction) {
        this.conditionedAction = conditionedAction;
    }

    /**
     * Sets new Gewünschtes Ergebnis der vorherigen Aktion.
     *
     * @param expectedResult New value of Gewünschtes Ergebnis der vorherigen Aktion.
     */
    public void setExpectedResult(boolean expectedResult) {
        this.expectedResult = expectedResult;
    }
}
