package de.uol.swp.common.game.card.parser.components.CardAction.types;

import de.uol.swp.common.game.card.parser.components.CardAction.CompositeCardAction;
import de.uol.swp.common.game.card.parser.components.CardAction.SimpleCardAction;

import java.util.ArrayList;

/**
 * Stellt den Spieler vor die Wahl, welche Aktion als nächstes ausgeführt werden soll.
 */
public class ChooseNextAction extends SimpleCardAction {

    /**
     * Die auswählbaren Aktionen
     */
    private ArrayList<CompositeCardAction> nextActions;
    /**
     * Die ausgewählte Aktion
     */
    private CompositeCardAction choosenAction;

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

    /**
     * Gibt die ausgwwählte Aktion zurück
     *
     * @return die ausgwwählte Aktion
     * @author KenoO
     * @since Sprint 6
     */
    public CompositeCardAction getChoosenAction() {
        return choosenAction;
    }
}
