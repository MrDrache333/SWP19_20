package de.uol.swp.common.game.messages;

import de.uol.swp.common.message.AbstractServerMessage;
import de.uol.swp.common.user.User;

import java.util.List;
import java.util.UUID;

public class CancelPoopBreakMessage extends AbstractServerMessage {

    private final User poopInitiator;
    private final UUID gameID;
    private final List<Boolean> votes;

    /**
     * Der Konstruktor der CancelPoopBreakMessage während der Pause.
     *
     * @param poopInitiator Der der auf Klo muss
     * @param gameID Die GameID
     * @author Keno S.
     * @since Sprint 10
     */
    public CancelPoopBreakMessage(User poopInitiator, UUID gameID) {
        this.poopInitiator = poopInitiator;
        this.gameID = gameID;
        this.votes = null;
    }

    /**
     * Der Konstruktor der CancelPoopBreakMessage während des Abstimmens.
     *
     * @param poopInitiator Der der auf Klo muss
     * @param gameID Die GameID
     * @param votes Die Abstimmungen
     * @author Keno S.
     * @since Sprint 10
     */
    public CancelPoopBreakMessage(User poopInitiator, UUID gameID, List<Boolean> votes) {
        this.poopInitiator = poopInitiator;
        this.gameID = gameID;
        this.votes = votes;
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

    /**
     * Gibt die aktuellen Votes zurück.
     *
     * @return Die Votes
     */
    public List<Boolean> getVotes() {
        return votes;
    }

}
