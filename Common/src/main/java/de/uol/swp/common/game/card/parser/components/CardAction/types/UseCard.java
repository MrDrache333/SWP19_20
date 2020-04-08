package de.uol.swp.common.game.card.parser.components.CardAction.types;

import de.uol.swp.common.game.card.parser.components.CardAction.SimpleCardAction;

/**
 * Benutzt eine Aktionskarte beliebig oft hintereinander.
 */
public class UseCard extends SimpleCardAction {

    private short count;    //Anzahl an ausführungen

    /**
     * Erstellt eine neue UseCard-Aktion.
     *
     * @param count Die Anzahl
     * @author KenoO
     * @since Sprint 6
     */
    public UseCard(short count) {
        this.count = count;
    }

    public short getCount() {
        return count;
    }

    /**
     * Sets new count.
     *
     * @param count New value of count.
     */
    public void setCount(short count) {
        this.count = count;
    }
}
