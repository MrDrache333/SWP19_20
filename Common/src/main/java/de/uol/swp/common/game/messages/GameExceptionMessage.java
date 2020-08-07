package de.uol.swp.common.game.messages;

import de.uol.swp.common.message.AbstractServerMessage;
import de.uol.swp.common.user.User;

import java.util.UUID;

public class GameExceptionMessage extends AbstractServerMessage {

    private static final long serialVersionUID = -8889082861061823738L;
    private final UUID gameID;
    private final String message;
    private User user;

    /**
     * Konstruktor der GameExceptionMessage
     *
     * @param gameID  die Game-ID
     * @param message die Nachricht
     * @author Julia
     * @since Sprint 6
     */
    public GameExceptionMessage(UUID gameID, User user , String message) {
        this.user = user;
        this.gameID = gameID;
        this.message = message;
    }

    /**
     * Gibt den Benutzer zurück
     *
     * @return message die Nachricht
     * @author Darian
     * @since Sprint 9
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
     * Gibt die Nachricht zurück
     *
     * @return message die Nachricht
     * @author Julia
     * @since Sprint 6
     */
    public String getMessage() {
        return message;
    }
}
