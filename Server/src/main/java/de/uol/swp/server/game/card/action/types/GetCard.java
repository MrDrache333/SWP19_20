package de.uol.swp.server.game.card.action.types;

import de.uol.swp.common.game.AbstractPlayground;
import de.uol.swp.common.game.card.parser.action.ComplexCardAction;

/**
 * Aktion um eine bestimmte Anzahl an Karten von einer Quelle zu holen.
 */
public class GetCard extends ComplexCardAction {

    /**
     * The Count.
     */
    private short count;    //Anzahl der Karten

    /**
     * Instantiates a new Get card.
     *
     * @param count the count
     * @author KenoO
     * @since
     */
    public GetCard(short count, AbstractPlayground.ZoneType source) {
        this.count = count;
        setCardSource(source);
    }

    @Override
    public boolean execute() {
        return false;
    }
}
