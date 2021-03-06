package de.uol.swp.common.game.messages;

import de.uol.swp.common.message.AbstractServerMessage;
import de.uol.swp.common.user.User;

import java.util.Map;
import java.util.UUID;

public class PoopBreakMessage extends AbstractServerMessage {

    private final User poopInitiator;
    private final User reqUser;
    private final UUID gameID;
    private final Map<User, Boolean> decisions;

    /**
     * Der Konstruktor der PoopBreakMessage.
     *
     * @param poopInitiator Der der auf Klo muss
     * @param gameID Die GameID
     * @param decisions Die aktuellen Entscheidungen
     * @author Keno S.
     * @since Sprint 10
     */
    public PoopBreakMessage(User poopInitiator, User reqUser, UUID gameID, Map<User, Boolean> decisions) {
        this.poopInitiator = poopInitiator;
        this.reqUser = reqUser;
        this.gameID = gameID;
        this.decisions = decisions;
    }

    /**
     * Gibt den Klogänger zurück.
     *
     * @return Den Klogänger
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
     * Gibt die aktuellen Entscheidungen zurück.
     *
     * @return Die Entscheidungen
     */
    public Map<User, Boolean> getDecisions() {
        return decisions;
    }

    /**
     * Gibt mit Hilfe von Streams alle zugestimmten Votes zurück.
     *
     * @return Die zugestimmten Votes
     */
    public int getAcceptedVotes() {
        return (int) decisions.values().stream().filter(d -> d).count();
    }

    /**
     * Gibt mit Hilfe von Streams alle abgelehnten Votes zurück.
     *
     * @return Die abgelehnten Votes
     */
    public int getDeclinedVotes() {
        return (int) decisions.values().stream().filter(d -> !d).count();
    }

    /**
     * Gibt den User zurück, der den Request gesendet hat.
     *
     * @return Der reqUser
     */
    public User getReqUser() {
        return reqUser;
    }
}
