package de.uol.swp.common.game.messages;

import de.uol.swp.common.game.AbstractPlayground;
import de.uol.swp.common.message.AbstractServerMessage;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Eine Message die dem Client mitteilt, welche Karten wohin bewegt werden müssen für die Animation.
 *
 * @author Ferit
 * @since Sprint6
 */

public class CardMovedMessage extends AbstractServerMessage {

    private ArrayList<Short> cardsWhichMoved;
    private AbstractPlayground.ZoneType from;
    private AbstractPlayground.ZoneType where;
    private UUID gameID;

    public CardMovedMessage(ArrayList<Short> cardsWhichMoved, AbstractPlayground.ZoneType from, AbstractPlayground.ZoneType where, UUID gameID) {
        this.cardsWhichMoved = cardsWhichMoved;
        this.from = from;
        this.where = where;
        this.gameID = gameID;
    }

    public ArrayList<Short> getCardsWhichMoved() {
        return cardsWhichMoved;
    }

    public AbstractPlayground.ZoneType getFrom() {
        return from;
    }

    public AbstractPlayground.ZoneType getWhere() {
        return where;
    }

    public UUID getGameID() {
        return gameID;
    }
}
