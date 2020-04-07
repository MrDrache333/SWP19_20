package de.uol.swp.common.game.card.parser.components.CardAction.types;

import de.uol.swp.common.game.card.Card;
import de.uol.swp.common.game.card.parser.components.CardAction.ComplexCardAction;
import de.uol.swp.common.game.card.parser.components.CardAction.Value;

import java.util.ArrayList;

/**
 * Aktion, welche den Benutzer eine festgelegte Anzahl an Karten auswählen lässt
 */
public class ChooseCard extends ComplexCardAction {

    /**
     * Anzahl der Karten
     */
    private Value count;
    /**
     * Ausgewählte Karten
     */
    private ArrayList<Card> choosenCards;

    /**
     * Erstellt eine neue Aktion mit einem Festen Wert an wählbaren Karten
     *
     * @param count Die Anzahl
     * @author KenoO
     * @since Sprint 6
     */
    public ChooseCard(short count) {
        this.count = new Value(count);
    }

    /**
     * Erstellt eine neue Aktion mit einem bereich an wählbaren Karten
     *
     * @param countMin Die Mindestanzahl
     * @param countMax Die Maximalanzahl
     * @author KenoO
     * @since Sprint 6
     */
    public ChooseCard(short countMin, short countMax) {
        this.count = new Value(countMin, countMax);
    }


    @Override
    public boolean execute() {
        return false;
    }

    /**
     * Gibt die ausgewählten Karten zurück
     *
     * @return Die ausgewählten Karten
     * @author KenoO
     * @since Sprint 6
     */
    public ArrayList<Card> getChoosenCards() {
        return choosenCards;
    }

    public Value getCount() {
        return count;
    }
}
