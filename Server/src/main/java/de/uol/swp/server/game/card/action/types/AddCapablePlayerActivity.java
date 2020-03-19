package de.uol.swp.server.game.card.action.types;

import de.uol.swp.common.game.AbstractPlayground;
import de.uol.swp.common.game.card.parser.action.SimpleCardAction;

/**
 * Fügt mögliche Aktivitäten des Spielers hinzu bzw. zieht welche ab.
 */
public class AddCapablePlayerActivity extends SimpleCardAction {

    private short count;    //Anzahl der zusätzlichen/ abzuziehenden Aktivitäten
    private AbstractPlayground.PlayerActivityValue activity;

    /**
     * Erstellt eine neue AddCapablePlayerActivity-Aktion.
     *
     * @param count Anzahl
     * @author KenoO
     * @since Sprint 6
     */
    public AddCapablePlayerActivity(short count, AbstractPlayground.PlayerActivityValue activity) {
        this.count = count;
        this.activity = activity;
    }

    @Override
    public boolean execute() {
        //TODO
        return false;
    }
}
