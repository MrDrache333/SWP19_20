package de.uol.swp.common.game.messages;

import de.uol.swp.common.message.AbstractServerMessage;
import de.uol.swp.common.user.User;

import java.util.UUID;

public class StartPoopBreakMessage extends AbstractServerMessage {

    private final User poopInitiator;
    private final UUID gameID;

    /**
     * Der Konstruktor der StartPoopBreakMessage.
     *
     * @param poopInitiator Der der auf Klo muss
     * @param gameID Die GameID
     * @author Keno S.
     * @since Sprint 10
     */
    public StartPoopBreakMessage(User poopInitiator, UUID gameID) {
        this.poopInitiator = poopInitiator;
        this.gameID = gameID;
    }

    /**
     * Gibt den Klogänger zurück.
     *
     * @return Der Klogänger
     */
    public User getPoopInitiator() {
        return poopInitiator;
    }

    /**
     * Gibt die GameID zurück.
     *
     * @return Die GameID
     */
    public UUID getGameID() {
        return gameID;
    }
}
