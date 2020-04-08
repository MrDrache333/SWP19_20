package de.uol.swp.common.game.card.parser.components.CardAction.types;

import de.uol.swp.common.game.card.parser.components.CardAction.CardAction;
import de.uol.swp.common.game.card.parser.components.CardAction.SimpleCardAction;

import java.util.ArrayList;

/**
 * Stellt den Spieler vor die Wahl, welche Aktion als nächstes ausgeführt werden soll.
 */
public class ChooseNextAction extends SimpleCardAction {

    /**
     * Die auswählbaren Aktionen
     */
    private ArrayList<CardAction> nextActions;
    /**
     * Die ausgewählte Aktion
     */
    private CardAction choosenAction;

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
     * Gibt die ausgwwählte Aktion zurück
     *
     * @return die ausgwwählte Aktion
     * @author KenoO
     * @since Sprint 6
     */
    public CardAction getChoosenAction() {
        return choosenAction;
    }

    public ArrayList<CardAction> getNextActions() {
        return nextActions;
    }


    /**
     * Sets new Die ausgewählte Aktion.
     *
     * @param choosenAction New value of Die ausgewählte Aktion.
     */
    public void setChoosenAction(CardAction choosenAction) {
        this.choosenAction = choosenAction;
    }

    /**
     * Sets new Die auswählbaren Aktionen.
     *
     * @param nextActions New value of Die auswählbaren Aktionen.
     */
    public void setNextActions(ArrayList<CardAction> nextActions) {
        this.nextActions = nextActions;
    }
}
