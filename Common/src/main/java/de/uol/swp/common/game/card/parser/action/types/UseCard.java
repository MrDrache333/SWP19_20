package de.uol.swp.common.game.card.parser.action.types;

import de.uol.swp.common.game.card.parser.action.SimpleCardAction;

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

    @Override
    public boolean execute() {
        return false;
    }
}
