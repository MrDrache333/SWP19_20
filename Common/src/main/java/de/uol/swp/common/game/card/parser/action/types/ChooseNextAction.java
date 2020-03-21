package de.uol.swp.common.game.card.parser.action.types;

import de.uol.swp.common.game.card.parser.action.CompositeCardAction;
import de.uol.swp.common.game.card.parser.action.SimpleCardAction;

import java.util.ArrayList;

/**
 * Stellt den Spieler vor die Wahl, welche Aktion als nächstes ausgeführt werden soll.
 */
public class ChooseNextAction extends SimpleCardAction {

    private ArrayList<CompositeCardAction> nextActions;
    private CompositeCardAction choosenAction;   //Ausgewählte Aktion

    /**
     * Erstellt eine neue ChooseNextAction-Aktion
     *
     * @param nextActions Die nächsten Aktionen
     * @author KenoO
     * @since Sprint 6
     */
    public ChooseNextAction(ArrayList<CompositeCardAction> nextActions) {
        this.nextActions = nextActions;
    }

    @Override
    public boolean execute() {
        //TODO
        return false;
    }

    public CompositeCardAction getChoosenAction() {
        return choosenAction;
    }
}
