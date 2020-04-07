package de.uol.swp.common.game.messages;

import de.uol.swp.common.game.AbstractPlayground;
import de.uol.swp.common.game.card.parser.components.CardAction.Value;
import de.uol.swp.common.message.AbstractServerMessage;

/**
 * Eine Nachricht die vom Server versendet wird und dem Client mitteilt, dass der Spieler Karten von der Hand auswählen muss und was mit diesen passiert.
 *
 * @author Ferit
 * @since Sprint6
 */
public class SelectCardsFromHandMessage extends AbstractServerMessage {
    private Value amountOfCardsToSelect;
    private AbstractPlayground.ZoneType from;
    private AbstractPlayground.ZoneType where;

    public SelectCardsFromHandMessage(Value amountOfCardsToSelect, AbstractPlayground.ZoneType from, AbstractPlayground.ZoneType where) {
        this.amountOfCardsToSelect = amountOfCardsToSelect;
        this.from = from;
        this.where = where;
    }

    public Value getAmountOfCardsToSelect() {
        return amountOfCardsToSelect;
    }

    public AbstractPlayground.ZoneType getFrom() {
        return from;
    }

    public AbstractPlayground.ZoneType getWhere() {
        return where;
    }
}
