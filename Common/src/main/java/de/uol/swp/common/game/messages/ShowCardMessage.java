package de.uol.swp.common.game.messages;

import de.uol.swp.common.game.AbstractPlayground;
import de.uol.swp.common.message.AbstractServerMessage;

/**
 * Message, die gesendet wird, wenn eine Karte aufgedeckt werden soll.
 *
 * @author Julia
 * @since Sprint6
 */
public class ShowCardMessage extends AbstractServerMessage {

    private static final long serialVersionUID = 6597124709953167310L;
    private short cardID;
    private AbstractPlayground.ZoneType zone;

    public ShowCardMessage(short cardID, AbstractPlayground.ZoneType zone) {
        this.cardID = cardID;
        this.zone = zone;
    }

    public short getCardID() {
        return cardID;
    }

    public AbstractPlayground.ZoneType getZone() {
        return zone;
    }
}
