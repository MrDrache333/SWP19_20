package de.uol.swp.common.game.card.parser.components.CardAction.types;

import de.uol.swp.common.game.card.parser.components.CardAction.SimpleCardAction;

/**
 * Benutzt eine Aktionskarte beliebig oft hintereinander.
 */
public class UseCard extends SimpleCardAction {

    private short count;    //Anzahl an ausführungen
    private short cardId = 0;

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

    /**
     * Gets cardId.
     *
     * @return Value of cardId.
     */
    public short getCardId() {
        return cardId;
    }

    /**
     * Sets new cardId.
     *
     * @param cardId New value of cardId.
     */
    public void setCardId(short cardId) {
        this.cardId = cardId;
    }
}
