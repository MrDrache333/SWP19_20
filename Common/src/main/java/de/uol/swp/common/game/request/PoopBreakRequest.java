package de.uol.swp.common.game.request;

import de.uol.swp.common.message.AbstractRequestMessage;
import de.uol.swp.common.user.User;

import java.util.UUID;

public class PoopBreakRequest extends AbstractRequestMessage {

    private static final long serialVersionUID = 4438745468468138864L;
    private final User user;
    private final User poopInitiator;
    private final UUID gameID;
    private final boolean poopDecision;

    /**
     * Der Konstruktor für den Klogänger.
     *
     * @param user Der Klogänger
     * @param gameID Die GameID
     */
    public PoopBreakRequest(User user, UUID gameID) {
        this.poopInitiator = user;
        this.user = user;
        this.gameID = gameID;
        this.poopDecision = true;
    }

    /**
     * Der Konstruktor für alle anderen Mitspieler
     *
     * @param user Der Mitspieler
     * @param gameID Die GameID
     * @param poopDecision Der Vote
     */
    public PoopBreakRequest(User user, UUID gameID, boolean poopDecision) {
        poopInitiator = null;
        this.user = user;
        this.gameID = gameID;
        this.poopDecision = poopDecision;
    }

    /**
     * Gibt den aktuellen Vote des Spielers zurück.
     *
     * @return Der Vote
     */
    public boolean getPoopDecision() {
        return poopDecision;
    }

    /**
     * Gibt den User zurück.
     *
     * @return Der User
     */
    public User getUser() {
        return user;
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
     * Gibt den Klogänger zurück, falls der anfragende der Klogänger ist, sonst null.
     *
     * @return Der Klogänger
     */
    public User getPoopInitiator() {
        return poopInitiator;
    }
}
