package de.uol.swp.server.game.card.action.types;

import de.uol.swp.common.game.card.parser.action.CardAction;
import de.uol.swp.common.game.card.parser.action.SimpleCardAction;

import java.util.ArrayList;

/**
 * Stellt den Spieler vor die Wahl, welche Aktion als nächstes ausgeführt werden soll.
 */
public class ChooseNextAction extends SimpleCardAction {

    private ArrayList<CardAction> nextActions;

    /**
     * Erstellt eine neue ChooseNextAction-Aktion
     *
     * @param nextActions Die nächsten Aktionen
     * @author KenoO
     * @since Sprint 6
     */
    public ChooseNextAction(ArrayList<CardAction> nextActions) {
        this.nextActions = nextActions;
    }

    /**
     * Erstellt eine neue ChooseNextAction-Aktion
     *
     * @param nextAction Die nächste Aktion
     * @author KenoO
     * @since Sprint 6
     */
    public ChooseNextAction(CardAction nextAction) {
        nextActions = new ArrayList<>();
        nextActions.add(nextAction);
    }

    @Override
    public boolean execute() {
        //TODO
        return false;
    }
}
