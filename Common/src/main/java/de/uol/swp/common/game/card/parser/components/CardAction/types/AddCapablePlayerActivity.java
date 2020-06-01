package de.uol.swp.common.game.card.parser.components.CardAction.types;

import de.uol.swp.common.game.AbstractPlayground;
import de.uol.swp.common.game.card.parser.components.CardAction.SimpleCardAction;

/**
 * Fügt mögliche Aktivitäten des Spielers hinzu bzw. zieht welche ab.
 */
public class AddCapablePlayerActivity extends SimpleCardAction {

    private short count;    //Anzahl der zusätzlichen/ abzuziehenden Aktivitäten
    private AbstractPlayground.PlayerActivityValue activity;

    /**
     * Erstellt eine neue AddCapablePlayerActivity-Aktion.
     *
     * @param count    Anzahl
     * @param activity Die neue Aktivität
     * @author KenoO
     * @since Sprint 6
     */
    public AddCapablePlayerActivity(short count, AbstractPlayground.PlayerActivityValue activity) {
        this.count = count;
        this.activity = activity;
    }

    public short getCount() {
        return count;
    }

    public AbstractPlayground.PlayerActivityValue getActivity() {
        return activity;
    }

    public void setCount(short count) {
        this.count = count;
    }

    public void setActivity(AbstractPlayground.PlayerActivityValue activity) {
        this.activity = activity;
    }
}
