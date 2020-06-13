package de.uol.swp.common.game.messages;

import de.uol.swp.common.message.AbstractServerMessage;
import de.uol.swp.common.user.User;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * Message die signalisiert, dass der User mit der Aktionsphase starten kann
 *
 * @author Julia
 * @since Sprint 5
 */
public class StartActionPhaseMessage extends AbstractServerMessage {

    private static final long serialVersionUID = -7186103893383553989L;
    private final User user;
    private final UUID gameID;
    private final Timestamp timestamp;

    /**
     * Der Konstruktor der StartActionPhaseMessage
     *
     * @param user      der User
     * @param gameID    die Game-ID
     * @param timestamp der Zeitstempel
     * @author Julia, Ferit
     * @since Sprint 6
     */
    public StartActionPhaseMessage(User user, UUID gameID, Timestamp timestamp) {
        this.user = user;
        this.gameID = gameID;
        this.timestamp = timestamp;
    }

    /**
     * Gibt den User zurück
     *
     * @return user der User
     * @author Julia
     * @since Sprint 6
     */
    public User getUser() {
        return user;
    }

    /**
     * Gibt die Game-ID zurück
     *
     * @return gameID die Game-ID
     * @author Julia
     * @since Sprint 6
     */
    public UUID getGameID() {
        return gameID;
    }

    /**
     * Gibt den Zeitstempel zurück
     *
     * @return timestamp der Zeitstempel
     * @author Ferit
     * @since Sprint 6
     */
    public Timestamp getTimestamp() {
        return timestamp;
    }
}
