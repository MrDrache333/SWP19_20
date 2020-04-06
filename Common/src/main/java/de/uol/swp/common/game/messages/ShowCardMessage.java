package de.uol.swp.common.game.messages;

import de.uol.swp.common.game.AbstractPlayground;
import de.uol.swp.common.message.AbstractServerMessage;

import java.util.UUID;

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
    private UUID gameID;

    public ShowCardMessage(short cardID, AbstractPlayground.ZoneType zone, UUID gameID) {
        this.cardID = cardID;
        this.zone = zone;
        this.gameID = gameID;
    }

    public short getCardID() {
        return cardID;
    }

    public AbstractPlayground.ZoneType getZone() {
        return zone;
    }

    public UUID getGameID() {
        return gameID;
    }
}
